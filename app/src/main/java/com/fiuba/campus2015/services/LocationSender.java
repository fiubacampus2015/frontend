package com.fiuba.campus2015.services;

import com.fiuba.campus2015.dto.user.Position;

public class LocationSender
{

    public static void send(final String token, final String user, final String location)
    {

        PoolingService.GetResult result = new PoolingService.GetResult<retrofit.client.Response, IApiUser>() {
            @Override
            public retrofit.client.Response getResult(IApiUser service) {
                return service.sendLocation(token, user, new Position(location));
            }
        };

        RestClient restClient = new RestClient();

        PoolingService callApi = new PoolingService<retrofit.client.Response, IApiUser>();
        callApi.fetch(restClient.getApiService(), result, new com.fiuba.campus2015.services.Response());

    }

}
