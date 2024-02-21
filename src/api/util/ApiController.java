package api.util;

public interface ApiController {
    void registerRoutes(RouterManager routerManager);
    String getPathPrefix();
}
