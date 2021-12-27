package org.talend.sdk.component.studio.websocket;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;

public class MockWebSocket implements ServicesClient.WebSocketClient {

    private final MockReponseFile fileGetter;

    @FunctionalInterface
    interface MockReponseFile {
        File getMockResponse(final String uri);
    }

    public static class DefaultFixFile implements MockReponseFile {
        private final String fileName;

        public DefaultFixFile(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public File getMockResponse(String uri) {
            final URL resource = Thread.currentThread().getContextClassLoader().getResource("websockettest");
            return new File(resource.getPath(), this.fileName);
        }
    }

    public MockWebSocket(final MockReponseFile fileGetter) {
        this.fileGetter = fileGetter;
    }

    public MockWebSocket(final String fileName) {
        this(new DefaultFixFile(fileName));
    }

    @Override
    public <T> T sendAndWait(final String id,
                             final String uri,
                             final Object payload,
                             final Class<T> expectedResponse,
                             final boolean doCheck) {
        final File mockSource = this.fileGetter.getMockResponse(uri);
        return MockWebSocket.handle(mockSource, expectedResponse);
    }

    @Override
    public void close() {
    }

    public static <T> T handle(final File mockSource,
                               final Class<T> expectedResponse) {
        try {
            final byte[] bytes = Files.readAllBytes(mockSource.toPath());
            final byte[] allByte = new byte[bytes.length + 2];
            System.arraycopy(bytes, 0, allByte, 0, bytes.length);
            allByte[bytes.length] = '^';
            allByte[bytes.length + 1] = '@';
            final PayloadHandler handler = new PayloadHandler();
            final ByteBuffer byteBuffer = ByteBuffer.wrap(allByte);
            handler.accept(byteBuffer);
            return handler.parseResponse(expectedResponse);
        } catch (IOException ex) {
            throw new UncheckedIOException("unreadable file " + mockSource.getPath(), ex);
        }
    }

}
