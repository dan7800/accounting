package accounting;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.IOException;

/**
 * I wonder what this class does.. something useful hopefully
 */
public class AccountingView {

    private static Configuration config = null;

    private static Configuration createConfig() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);

        // Specify the source where the template files come from.
        cfg.setClassLoaderForTemplateLoading(Thread.currentThread().getContextClassLoader(),"templates");

        // Set the preferred charset template files are stored in. UTF-8 is
        // a good choice in most applications:
        cfg.setDefaultEncoding("UTF-8");

        // Sets how errors will appear.
        // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

        // Don't log exceptions inside FreeMarker that it will thrown at you anyway:
        cfg.setLogTemplateExceptions(false);

        return cfg;
    }

    private static Configuration getConfiguration() {
        if ( config == null ) config = createConfig();
        return config;
    }

    public static Template getTemplate() {
        Template temp = null;
        try {
            temp = getConfiguration().getTemplate("views/main.ftl");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
