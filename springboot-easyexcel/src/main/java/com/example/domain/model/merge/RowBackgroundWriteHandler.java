package com.example.domain.model.merge;

import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

/**
 * @author Jonny
 * @description 自定义行样式 由于CellStyle超过64000会报错，所以需要将样式对象缓存起来，避免重复样式
 */
public class RowBackgroundWriteHandler implements CellWriteHandler {

    /**
     * 根据条件设置单元格样式： 小于60分单元格标红
     *
     * @param context 上下文
     */
    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        Cell cell = context.getCell();
        // 数据行奇数行设置背景
        if (context.getRowIndex() > 0 && (cell.getRowIndex() & 1) == 0) {

            Workbook workbook = context.getWriteWorkbookHolder().getWorkbook();
            CellStyle cellStyle = workbook.createCellStyle();

            // WriteCellData<?> cellData = context.getFirstCellData();
            // WriteCellStyle originalStyle  = cellData.getOrCreateStyle();
            // WriteFont writeFont = originalStyle.getWriteFont();

            // 原有样式
            CellStyle originalStyle = cell.getCellStyle();

            // 设置rgb颜色
            byte[] rgb = new byte[]{(byte) 255, (byte) 230, (byte) 153};
            XSSFCellStyle xssfCellColorStyle = (XSSFCellStyle) cellStyle;
            xssfCellColorStyle.setFillForegroundColor(new XSSFColor(rgb, null));
            // 指定固体填充 FillPatternType 为FillPatternType.SOLID_FOREGROUND
            cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            // // 设置字体样式（可选）
            Font originalFont = workbook.getFontAt(originalStyle.getFontIndexAsInt());
            Font newFont = workbook.createFont();
            newFont.setFontHeightInPoints(originalFont.getFontHeightInPoints());
            newFont.setFontName(originalFont.getFontName());
            newFont.setColor(IndexedColors.RED.getIndex());
            cellStyle.setFont(newFont);

            cell.setCellStyle(xssfCellColorStyle);


            // 清除默认样式，不设置则无效
            context.getFirstCellData().setWriteCellStyle(null);
        }
    }

}
