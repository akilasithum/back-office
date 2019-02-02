package com.back.office.framework;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;

import java.io.IOException;

public class OnDemandFileDownloader extends FileDownloader
{
    public interface OnDemandStreamResource extends StreamResource.StreamSource
    {
        String getFilename();
    }

    private final OnDemandStreamResource onDemandStreamResource;

    public OnDemandFileDownloader(OnDemandStreamResource onDemandStreamResource)
    {
        super(new StreamResource(onDemandStreamResource, ""));
        this.onDemandStreamResource = onDemandStreamResource;
    }

    @Override
    public boolean handleConnectorRequest(VaadinRequest request,
                                          VaadinResponse response, String path) throws IOException
    {
        getResource().setFilename(onDemandStreamResource.getFilename());
        return super.handleConnectorRequest(request, response, path);
    }

    private StreamResource getResource()
    {
        return (StreamResource) this.getResource("dl");
    }
}
