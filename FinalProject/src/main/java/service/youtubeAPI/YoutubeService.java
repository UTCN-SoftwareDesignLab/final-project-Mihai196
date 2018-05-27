package service.youtubeAPI;

import com.google.api.services.youtube.model.SearchResult;

import java.util.List;

public interface YoutubeService {
    List<SearchResult> getSearchResult(String queryTerm);
}
