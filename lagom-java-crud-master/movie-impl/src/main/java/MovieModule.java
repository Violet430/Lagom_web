import com.google.inject.AbstractModule;
import com.knoldus.MovieService;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

public class MovieModule extends AbstractModule implements ServiceGuiceSupport {
    @Override
    protected void configure() {
        bindServices(serviceBinding(MovieService.class,MovieServiceImpl.class));
    }
}
