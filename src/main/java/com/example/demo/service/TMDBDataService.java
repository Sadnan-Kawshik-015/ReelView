package com.example.demo.service;

import com.example.demo.dtos.external.TMDBResponseDTO;
import com.example.demo.dtos.external.TMDBVideoDTO;
import com.example.demo.dtos.external.TMDBVideoResultsDTO;
import com.example.demo.utils.customexceptions.RequiredResourceNotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TMDBDataService extends DataService{
    @Value("${tmdb.base.url}")
    protected String tmdbBaseUrl;
    @Value("${tmdb.api.key}")
    protected String tmdbApiKey;
    @Value("${tmdb.image.base.url}")
    protected String tmdbImageBaseUrl;
    @Value("${tmdb.video.base.url.youtube}")
    protected String tmdbVideoBaseUrlYoutube;

    @SneakyThrows
    public TMDBResponseDTO getMovieDetails(Integer movieId){
        String url = tmdbBaseUrl + "/movie/" + movieId + "?api_key=" + tmdbApiKey + "&append_to_response=videos";
         return apiCall(url, TMDBResponseDTO.class,false,"").getBody();

    }
    public Map<String,String> fetchAdditionalData(Integer movieId) throws Exception {
        try {
            TMDBResponseDTO responseDTO = getMovieDetails(movieId);
            Map<String, String> additionalData = new HashMap<>();
            if (responseDTO != null) {
                additionalData.put("poster_path", tmdbImageBaseUrl +"/w500"+ responseDTO.getPoster_path());
                additionalData.put("backdrop_path", tmdbImageBaseUrl +"/original"+ responseDTO.getBackdrop_path());
                List<TMDBVideoResultsDTO> videoResults = responseDTO.getVideos().getResults();
                if (videoResults != null && !videoResults.isEmpty()) {
                    for (TMDBVideoResultsDTO video : videoResults) {
                        if ("YouTube".equalsIgnoreCase(video.getSite()) && "Trailer".equalsIgnoreCase(video.getType())) {
                            additionalData.put("trailer_path", tmdbVideoBaseUrlYoutube + video.getKey());
                            break;
                        }
                    }
                }
            }
            return additionalData;
        }catch (Exception e){
            processException(e);
        }
        return null;
    }
}
