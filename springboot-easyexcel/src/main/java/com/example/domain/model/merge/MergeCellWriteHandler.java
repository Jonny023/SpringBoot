package com.example.domain.model.merge;

import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.context.CellWriteHandlerContext;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 * @author Jonny
 * @description 自定义单元格样式
 */
public class MergeCellWriteHandler implements CellWriteHandler {

    /**
     * 由于CellStyle超过64000会报错，所以需要将样式对象缓存起来，避免重复样式
     */
    private static final WriteCellStyle DEFAULT_STYLE;

    static {
        DEFAULT_STYLE = getWriteCellStyle();
    }

    /**
     * 去除边框+内容居右
     *
     * @param context 上下文
     */
    @Override
    public void afterCellDispose(CellWriteHandlerContext context) {
        if (context.getRowIndex() == 0) {
            // 设置样式
            context.getCellDataList().get(0).setWriteCellStyle(DEFAULT_STYLE);
        }
    }

    /**
     * 构建样式
     *
     * @return 样式
     */
    private static WriteCellStyle getWriteCellStyle() {
        // 创建样式
        WriteCellStyle writeCellStyle = new WriteCellStyle();
        // 设置内容居右
        writeCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        // 设置垂直居中
        writeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 设置边框为无
        writeCellStyle.setBorderLeft(BorderStyle.NONE);
        writeCellStyle.setBorderTop(BorderStyle.NONE);
        writeCellStyle.setBorderRight(BorderStyle.NONE);
        writeCellStyle.setBorderBottom(BorderStyle.NONE);
        return writeCellStyle;
    }

}
