package code.name.monkey.retromusic;

import code.name.monkey.retromusic.providers.RepositoryImpl;
import code.name.monkey.retromusic.providers.interfaces.Repository;
import code.name.monkey.retromusic.rest.KogouClient;
import code.name.monkey.retromusic.rest.service.KuGouApiService;
import code.name.monkey.retromusic.util.schedulers.BaseSchedulerProvider;
import code.name.monkey.retromusic.util.schedulers.SchedulerProvider;

public class Injection {

    public static Repository provideRepository() {
        return RepositoryImpl.getInstance();
    }

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }

    public static KuGouApiService provideKuGouApiService() {
        return new KogouClient().getApiService();
    }
}
