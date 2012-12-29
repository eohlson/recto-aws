package eu.ohlson;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

public class WebRunner {

    public static void main(String[] args) throws Exception {
        new WebRunner().start();

    }

    public void start() throws Exception {
        URL webDirUrl = WebRunner.class.getResource("/web/");
        System.out.println(webDirUrl);
        String webDir = webDirUrl.toExternalForm();

        Server server = new Server(8080);

        // Create a resource handler for static content.
        ResourceHandler staticResourceHandler = new ResourceHandler();
        staticResourceHandler.setResourceBase(webDir);
        staticResourceHandler.setDirectoriesListed(true);
        //staticResourceHandler.setWelcomeFiles(new String[]{ "index.html",  });
        //staticResourceHandler.setCacheControl("no-store,no-cache,must-revalidate");

        // Create context handler for static resource handler.
        ContextHandler staticContextHandler = new ContextHandler();
        staticContextHandler.setContextPath("/static");
        staticContextHandler.setHandler(staticResourceHandler);

        // Create servlet context handler for main servlet.
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.setContextPath("/");
        servletContextHandler.addServlet(new ServletHolder(new HelloServlet()),"/");

        // Create a handler list to store our static and servlet context handlers.
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { staticContextHandler, servletContextHandler });

        // Add the handlers to the server and start jetty.
        server.setHandler(handlers);
        server.start();
        server.join();
    }

    private class HelloServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            PrintWriter out = resp.getWriter();
            out.println("Hello World");
        }
    }
}
