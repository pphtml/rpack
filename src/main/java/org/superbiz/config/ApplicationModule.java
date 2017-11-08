package org.superbiz.config;

import com.google.inject.Provides;
import com.google.inject.Scopes;
import org.superbiz.dao.UserDAO;
import org.superbiz.dao.UserDAOImpl;
import org.superbiz.service.EmployeeRepository;
import org.superbiz.service.EmployeeService;
import org.superbiz.service.EmployeeServiceImpl;
import org.superbiz.service.RegexService;
import org.superbiz.service.RegexServiceImpl;
import org.superbiz.service.TaskExecutor;
import org.superbiz.service.TaskExecutorImpl;
import org.superbiz.service.UserService;
import org.superbiz.service.UserServiceImpl;
import ratpack.guice.ConfigurableModule;

import javax.inject.Singleton;

public class ApplicationModule extends ConfigurableModule<ApplicationModule.Config> {
    public static class Config {
        private String nodeName;

        public String getNodeName() {
            return nodeName;
        }

        public void setNodeName(String nodeName) {
            this.nodeName = nodeName;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("Config{");
            sb.append("nodeName='").append(nodeName).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }

    @Override
    protected void configure() {
        bind(UserDAO.class).to(UserDAOImpl.class).in(Scopes.SINGLETON);
        bind(RegexService.class).to(RegexServiceImpl.class).in(Scopes.SINGLETON);
        bind(TaskExecutor.class).to(TaskExecutorImpl.class).in(Scopes.SINGLETON);
        // bind(UserService.class).to(UserServiceImpl.class).asEagerSingleton();
    }

    @Provides
    UserService userService(UserDAO userDAO, Config config) {
        return new UserServiceImpl(userDAO, config);
    }

    @Provides
    @Singleton
    public EmployeeService employeeService(EmployeeRepository employeeRepository) {
        return new EmployeeServiceImpl(employeeRepository);
    }

}
