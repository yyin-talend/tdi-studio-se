package org.talend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelTool {

    private Workbook wb = null;

    private Workbook preWb = null;

    private String sheetName = null;

    private Sheet sheet = null;

    private Sheet preSheet = null;

    private Row curRow = null;

    private Row preRow = null;

    private Cell curCell = null;

    private Cell preCell = null;

    private boolean appendWorkbook = false;

    private boolean appendSheet = false;

    private int startX = 0;

    private int curY = 0;

    private int xOffset = 0;

    private boolean isAbsY = false;

    private int absX = 0;

    private int absY = 0;

    private boolean keepCellFormat = false;

    private Font font = null;

    private Map<String, CellStyle> cellStylesMapping = null;

    public ExcelTool() {
        cellStylesMapping = new HashMap<String, CellStyle>();
    }

    public void setAppend(boolean appendWorkbook, boolean appendSheet) {
        this.appendWorkbook = appendWorkbook;
        this.appendSheet = appendSheet;
    }

    public void setXY(boolean isAbsY, int absX, int absY, boolean keepFormat) {
        this.isAbsY = isAbsY;
        this.absX = absX;
        this.absY = absY;
        this.keepCellFormat = keepFormat;
    }

    public void setSheet(String sheetName) {
        this.sheetName = sheetName;
    }

    public void prepareStream() throws Exception {
        wb = new XSSFWorkbook();
        sheet = wb.createSheet(sheetName);
        if (isAbsY) {
            startX = absX;
            curY = absY;
        }
    }

    public void prepareXlsxFile(String fileName) throws Exception {
        File xlsxFile = new File(fileName);
        if (xlsxFile.exists()) {
            if (isAbsY && keepCellFormat) {
                initPreXlsx(fileName);
            }
            InputStream inp = new FileInputStream(fileName);
            wb = WorkbookFactory.create(inp);
            if (appendWorkbook) {
                sheet = wb.getSheet(sheetName);
                if (sheet != null) {
                    if (appendSheet) {
                        curY = sheet.getLastRowNum() + 1;
                    } else {
                        wb.removeSheetAt(wb.getSheetIndex(sheetName));
                        sheet = wb.createSheet(sheetName);
                    }
                } else {
                    sheet = wb.createSheet(sheetName);
                }
            } else {
                List<Integer> indexs = new ArrayList<Integer>();
                for (XSSFSheet sheet : (XSSFWorkbook) wb) {
                    indexs.add(wb.getSheetIndex(sheet));
                }
                for (int i = indexs.size() - 1; i >= 0; i--) {
                    wb.removeSheetAt(indexs.get(i));
                }
                sheet = wb.createSheet(sheetName);
            }
        } else {
            wb = new XSSFWorkbook();
            sheet = wb.createSheet(sheetName);
        }
        if (isAbsY) {
            startX = absX;
            curY = absY;
        }
    }

    private void initPreXlsx(String fileName) throws Exception {
        InputStream preIns = new FileInputStream(fileName);
        preWb = WorkbookFactory.create(preIns);
        preSheet = preWb.getSheet(sheetName);
    }

    public void setFont(String fontName) {
        if (!"".equals(fontName)) {
            font = wb.createFont();
            font.setFontName(fontName);
        }
    }

    public void addRow() {
        if (isAbsY && keepCellFormat) {
            if (preSheet != null) {
                preRow = preSheet.getRow(curY);
            } else {
                preRow = null;
            }
        }
        curRow = sheet.getRow(curY);
        if (curRow == null)
            curRow = sheet.createRow(curY);
        curY = curY + 1;
        xOffset = 0;
    }

    private void addCell() {
        if (isAbsY && keepCellFormat) {
            if (preRow != null) {
                preCell = preRow.getCell(startX + xOffset);
            } else {
                preCell = null;
            }
        }
        curCell = curRow.createCell(startX + xOffset);
        xOffset++;
    }

    private CellStyle getNormalCellStyle() {
        CellStyle preCellStyle = getPreCellStyle();
        if (preCellStyle == null) {
            if (cellStylesMapping.get("normal") != null) {
                return cellStylesMapping.get("normal");
            } else {
                CellStyle style = wb.createCellStyle();
                if (font != null) {
                    style.setFont(font);
                }
                cellStylesMapping.put("normal", style);
                return style;
            }
        } else {
            return preCellStyle;
        }

    }

    private CellStyle getDateCellStyle(String pattern) {
        CellStyle preCellStyle = getPreCellStyle();
        if (preCellStyle == null) {
            if (cellStylesMapping.get(pattern) != null) {
                return cellStylesMapping.get(pattern);
            } else {
                CellStyle style = wb.createCellStyle();
                if (font != null) {
                    style.setFont(font);
                }
                if (pattern != null || !"".equals(pattern)) {
                    style.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat(pattern));
                }
                cellStylesMapping.put(pattern, style);
                return style;
            }
        } else {
            return preCellStyle;
        }
    }

    private CellStyle getPreCellStyle() {
        if (isAbsY && keepCellFormat) {
            if (preCell == null) {
                return null;
            } else {
                CellStyle preCellStyle = preCell.getCellStyle();
                CellStyle targetCellStyle = wb.createCellStyle();
                targetCellStyle.cloneStyleFrom(preCellStyle);
                return targetCellStyle;
            }
        } else {
            return null;
        }
    }

    public void addCellValue(boolean booleanValue) {
        addCell();
        curCell.setCellValue(booleanValue);
        curCell.setCellStyle(getNormalCellStyle());
    }

    public void addCellValue(Date dateValue, String pattern) {
        addCell();
        curCell.setCellValue(dateValue);
        curCell.setCellStyle(getDateCellStyle(pattern));
    }

    public void addCellValue(double doubleValue) {
        addCell();
        curCell.setCellValue(doubleValue);
        curCell.setCellStyle(getNormalCellStyle());
    }

    public void addCellValue(String stringValue) {
        addCell();
        curCell.setCellValue(stringValue);
        curCell.setCellStyle(getNormalCellStyle());
    }

    public void setColAutoSize(int colNum) {
        sheet.autoSizeColumn(startX + colNum, true);
    }

    public void writeExcel(OutputStream outputStream) throws Exception {
        wb.write(outputStream);
    }

    public void writeExcel(String fileName, boolean createDir) throws Exception {
        if (createDir) {
            File file = new File(fileName);
            File pFile = file.getParentFile();
            if (pFile != null && !pFile.exists()) {
                pFile.mkdirs();
            }
        }
        FileOutputStream fileOutput = new FileOutputStream(fileName);
        wb.write(fileOutput);
        fileOutput.close();
    }

    public static void main(String[] args) throws Exception {
        // ExcelTool tool = new ExcelTool();
        // tool.setSheet("sheet1");
        // tool.setAppend(false, false);
        // tool.setXY(true, 0, 0, true);
        // tool.prepareXlsxFile("c:/xlsxTest/out.xlsx");
        // tool.setFont("");
        // tool.addRow();
        // tool.addCellValue("hello world!!!");
        // tool.setColAutoSize(0);
        // tool.writeExcel("c:/xlsxTest/out.xlsx", true);

        // ExcelTool tool = new ExcelTool();
        // tool.setSheet("sheet1");
        // tool.setAppend(true, true);
        // tool.setXY(true, 0, 0, true);
        // tool.setFont("");
        // // tool.prepareXlsxFile("c:/xlsxTest/out.xlsx");
        // tool.addRow();
        // tool.addCellValue("hello world!!!!!!!");
        // tool.setColAutoSize(0);
        // OutputStream outputStream = new FileOutputStream("c:/xlsxTest/out.xlsx");
        // tool.writeExcel(outputStream);

        OutputStream out = new java.io.FileOutputStream("C:/xlsxTest/out.xlsx");
        ExcelTool tool = new ExcelTool();
        tool.setSheet("sheet1");
        tool.setAppend(false, false);
        tool.setXY(true, 0, 0, true);
        // tool.prepareXlsxFile("c:/xlsxTest/out.xlsx");
        tool.prepareStream();
        tool.setFont("");
        tool.addRow();
        tool.addCellValue("hello world!!!");
        tool.setColAutoSize(0);
        tool.writeExcel(out);

    }
}
