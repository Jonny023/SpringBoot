package com.example.domain.model.merge;

import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.math.BigDecimal;

/**
 * @author Jonny
 * @description 自定义单元格样式 由于CellStyle超过64000会报错，所以需要将样式对象缓存起来，避免重复样式
 */
public class CellBackgroundWriteHandler implements CellWriteHandler {

    /**
     * 根据条件设置单元格样式： 小于60分单元格标红
     * <p>
     * 方式1
     * 可以设置RGB样色，缺点就是会丢失单元格原本样式：比如字体样式，如需保留样式需要新建字体拷贝原有样式
     * <p>
     * 方式2
     * 设置预置样式，灵活度不够高，也会丢失单元格字体样式
     * <p>
     * 方式3
     * 设置预置背景色简单，是在原有样式上修改，优点是不会丢失字体样式，缺点是只能使用预置颜色
     *
     * @param context 上下文
     */
    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        Cell cell = context.getCell();
        // 第二行第3列
        if (context.getRowIndex() > 0 && cell.getColumnIndex() == 2) {
            if (CellType.NUMERIC == cell.getCellType()) {
                double numericCellValue = cell.getNumericCellValue();
                BigDecimal score = BigDecimal.valueOf(numericCellValue);
                BigDecimal passingScore = BigDecimal.valueOf(60);
                if (score.compareTo(passingScore) < 0) {
                    WriteCellData<?> cellData = context.getFirstCellData();

                    // Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
                    // CellStyle cellStyle = workbook.createCellStyle();

                    // ====================================方式1======================================
                    // 设置RGB样色
                    // byte[] rgb = new byte[]{(byte) 250, (byte) 0, (byte) 0};
                    // XSSFCellStyle xssfCellColorStyle = (XSSFCellStyle) cellStyle;
                    // xssfCellColorStyle.setFillForegroundColor(new XSSFColor(rgb, null));
                    // 指定固体填充 FillPatternType 为FillPatternType.SOLID_FOREGROUND
                    // cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    // cellStyle.setAlignment(HorizontalAlignment.CENTER);
                    // cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    // cell.setCellStyle(xssfCellColorStyle);

                    // ====================================方式2======================================
                    // 枚举类内置IndexedColors.RED.getIndex()
                    // // 原有样式
                    // CellStyle originalStyle = cell.getCellStyle();
                    // // 设置填充模式
                    // cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    // // 设置背景色
                    // cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
                    // // 原有字体对齐方式 水平居中 垂直居中
                    // // 设置字体样式（可选）
                    // Font originalFont = workbook.getFontAt(originalStyle.getFontIndexAsInt());
                    // Font newFont = workbook.createFont();
                    // newFont.setFontHeightInPoints(originalFont.getFontHeightInPoints());
                    // newFont.setFontName(originalFont.getFontName());
                    // newFont.setColor(IndexedColors.RED.getIndex());
                    // cellStyle.setFont(newFont);
                    // cell.setCellStyle(cellStyle);

                    // 清除默认样式，不设置则无效(适用于方式1，方式2)
                    // cellData.setWriteCellStyle(null);

                    // ====================================方式3======================================
                    // 这种方式不会丢失字体样式，仅修改背景色
                    WriteCellStyle originalStyle = cellData.getOrCreateStyle();
                    originalStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
                    originalStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
                }
            }
        }
    }

}
