package io.pivotal.pal.tracker.timesheets;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.web.client.RestOperations;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectClient {

    private final RestOperations restOperations;
    private final String endpoint;
    private final Map<Long,ProjectInfo> cachedPrjInfo = new ConcurrentHashMap<Long, ProjectInfo>();

    public ProjectClient(RestOperations restOperations, String registrationServerEndpoint) {
        this.restOperations = restOperations;
        this.endpoint = registrationServerEndpoint;
    }

    @HystrixCommand(fallbackMethod = "getProjectFromCache")
    public ProjectInfo getProject(long projectId) {
        ProjectInfo prjInfo = restOperations.getForObject(endpoint + "/projects/" + projectId, ProjectInfo.class);
        cachedPrjInfo.put(projectId,prjInfo);
        return prjInfo;
    }

    public ProjectInfo getProjectFromCache(long projectId){
        return cachedPrjInfo.get(projectId);
    }
}
