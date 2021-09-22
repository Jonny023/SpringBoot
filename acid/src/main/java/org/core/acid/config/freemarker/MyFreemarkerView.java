package org.core.acid.config.freemarker;

import org.core.acid.utils.RequestUtil;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *  freemarker全局变量配置
 */
public class MyFreemarkerView extends FreeMarkerView {

    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        String base = RequestUtil.getBasePath(request);
        model.put("base", base);
        super.exposeHelpers(model, request);
    }
}
