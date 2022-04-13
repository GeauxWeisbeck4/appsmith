package com.appsmith.server.controllers.ce;

import com.appsmith.server.constants.FieldName;
import com.appsmith.server.domains.Application;
import com.appsmith.server.dtos.ApplicationTemplate;
import com.appsmith.server.dtos.ResponseDTO;
import com.appsmith.server.services.ApplicationTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
public class ApplicationTemplateControllerCE {

    protected final ApplicationTemplateService applicationTemplateService;

    public ApplicationTemplateControllerCE(ApplicationTemplateService applicationTemplateService) {
        this.applicationTemplateService = applicationTemplateService;
    }

    @GetMapping
    public Mono<ResponseDTO<List<ApplicationTemplate>>> getAll() {
        return applicationTemplateService.getActiveTemplates().collectList()
                .map(templates -> new ResponseDTO<>(HttpStatus.OK.value(), templates, null));
    }

    @GetMapping("{templateId}")
    public Mono<ResponseDTO<ApplicationTemplate>> getTemplateDetails(@PathVariable String templateId) {
        return applicationTemplateService.getTemplateDetails(templateId)
                .map(templates -> new ResponseDTO<>(HttpStatus.OK.value(), templates, null));
    }

    @GetMapping("{templateId}/similar")
    public Mono<ResponseDTO<List<ApplicationTemplate>>> getSimilarTemplates(@PathVariable String templateId) {
        return applicationTemplateService.getSimilarTemplates(templateId).collectList()
                .map(templates -> new ResponseDTO<>(HttpStatus.OK.value(), templates, null));
    }

    @PostMapping("{templateId}/import/{organizationId}")
    public Mono<ResponseDTO<Application>> importApplicationFromTemplate(@PathVariable String templateId,
                                                           @PathVariable String organizationId) {
        return applicationTemplateService.importApplicationFromTemplate(templateId, organizationId)
                .map(importedApp -> new ResponseDTO<>(HttpStatus.OK.value(), importedApp, null));
    }

    @PostMapping("{templateId}/merge/{applicationId}")
    public Mono<ResponseDTO<Application>> mergeTemplateWithApplication(@PathVariable String templateId,
                                                                        @PathVariable String applicationId,
                                                                       @RequestHeader(name = FieldName.BRANCH_NAME, required = false) String branchName) {
        return applicationTemplateService.mergeTemplateWithApplication(templateId, applicationId, branchName)
                .map(importedApp -> new ResponseDTO<>(HttpStatus.OK.value(), importedApp, null));
    }
}
