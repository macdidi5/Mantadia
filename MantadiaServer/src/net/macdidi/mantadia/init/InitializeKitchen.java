package net.macdidi.mantadia.init;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import net.macdidi.mantadia.kitchen.model.KitchenModel;
import net.macdidi.mantadia.kitchen.model.KitchenModelImpl;

/**
 * 啟動與終止廚房通知服務
 * 
 * @author macdidi
 */
@WebListener()
public class InitializeKitchen implements ServletContextListener {
    
    private Registry registry;

    /**
     * 啟動廚房通知服務
     */
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();

        try {
            context.log("Mantadia RMI Server ready to bind...");
            
            // 取得註冊RMI服務的Registry物件
            registry = LocateRegistry.createRegistry(
                    KitchenModel.RMI_SERVICE_PORT);
            // 建立提供RMI服務的KitchenModel物件
            KitchenModel model = new KitchenModelImpl();
            // 註冊RMI服務
            registry.rebind(KitchenModel.RMI_SERVICE_NAME, model);
            
            // 設定Model物件為ServletContext範圍的Attribute
            context.setAttribute("KitchenModel", model);
            
            context.log("Mantadia RMI Server ready!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除廚房通知服務
     */
    public void contextDestroyed(ServletContextEvent event) {
        ServletContext context = event.getServletContext();

        try {
            context.log("Mantadia RMI Server ready to unbind...");

            // 移除RMI服務
            if (registry != null) {
                registry.unbind(KitchenModel.RMI_SERVICE_NAME);
                registry = null;
            }
            
            context.log("Mantadia RMI Server unbind!");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
