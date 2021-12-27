package org.talend.sdk.component.studio.websocket;

import org.talend.sdk.component.server.front.model.error.ErrorPayload;

import javax.json.bind.Jsonb;
import javax.json.bind.spi.JsonbProvider;
import javax.json.stream.JsonParsingException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

class PayloadHandler implements Consumer<ByteBuffer> {

    private static final byte[] EOM = "^@".getBytes(StandardCharsets.UTF_8);

    private static final Jsonb jsonb = JsonbProvider.provider("org.apache.johnzon.jsonb.JohnzonProvider").create().build();

    final CountDownLatch latch = new CountDownLatch(1);

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    private final byte[] lastBytes = new byte[2];

    @Override
    public void accept(final ByteBuffer byteBuffer) {
        try {
            final byte[] array = byteBuffer.array();
            if (array.length >= 2) {
                System.arraycopy(array, array.length - 2, lastBytes, 0, 2);
            } else if (array.length > 0) {
                lastBytes[0] = lastBytes[1];
                lastBytes[1] = array[0];
            }
            out.write(array);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        } finally {
            if (Arrays.equals(lastBytes, EOM)) {
                latch.countDown();
            }
        }
    }

    byte[] payload() {
        return payload(true);
    }

    private byte[] payload(final boolean failOnBadStatus) {
        final byte[] value = out.toByteArray();
        // todo: check status header and fail if > 399 with the error message in the
        // payload
        int start = 0;
        {
            // find the first empty line which means the payload starts
            boolean visitedEol = false;
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            for (int idx = 0; idx < value.length - 1; idx++) {
                if (value[idx] == '\r' && value[idx + 1] == '\n') {
                    if (failOnBadStatus) {
                        final String header = new String(baos.toByteArray(), StandardCharsets.UTF_8);
                        if (header.startsWith("status:")) {
                            try {
                                if (Integer.parseInt(header.substring("status:".length()).trim()) > 399) {
                                    final String response = new String(value);
                                    ErrorPayload errorPayload;
                                    try {
                                        errorPayload = PayloadHandler.parseResponse(payload(false), ErrorPayload.class);
                                    } catch (final IllegalArgumentException iae) {
                                        errorPayload = null;
                                    }
                                    throw new ServicesClient.ClientException(response, errorPayload);
                                }
                            } catch (final NumberFormatException nfe) {
                            }
                        }
                    }
                    // no-op: ignore this validation then
                    idx++;
                    if (visitedEol) {
                        start = idx + 1;
                        break;
                    }
                    visitedEol = true;
                    baos.reset();
                } else {
                    baos.write(value[idx]);
                    visitedEol = false;
                }
            }
        }
        final int len = value.length - EOM.length - start;
        if (len <= 0) {
            return new byte[0];
        }
        final byte[] payload = new byte[len];
        System.arraycopy(value, start, payload, 0, len);
        return payload;
    }

    <T> T parseResponse(final Class<T> expectedResponse) {
        return PayloadHandler.parseResponse(this.payload(), expectedResponse);
    }

    static <T> T parseResponse(final byte[] payload, final Class<T> expectedResponse) {
        if (expectedResponse.isInstance(payload)) {
            return expectedResponse.cast(payload);
        }
        if (String.class == expectedResponse) {
            return expectedResponse.cast(new String(payload, StandardCharsets.UTF_8));
        }
        try (InputStream stream = new ByteArrayInputStream(payload)) {
            return jsonb.fromJson(stream, expectedResponse);
        } catch (final JsonParsingException jpe) {
            throw new IllegalArgumentException("Can\'t parse JSON: \'" + new String(payload, StandardCharsets.UTF_8) + "\'", jpe);
        } catch (final IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
