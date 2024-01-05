package com.sportsecho.game.service;

import com.sportsecho.common.dto.ApiResponse;
import com.sportsecho.common.dto.ResponseCode;
import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.game.dto.GameResponseDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.sportsecho.global.exception.ErrorCode;
@RequiredArgsConstructor
@Service
public class GameService {

    private final RestTemplate restTemplate;

    public ApiResponse<List<GameResponseDto>> getGamesBySport(String sportType) {
        String apiUrl = determineApiUrl(sportType);
        ResponseEntity<ApiResponse> response = restTemplate.getForEntity(apiUrl, ApiResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            List<GameResponseDto> gameList = convertToGameResponseDtoList(response.getBody().getData());
            return ApiResponse.of(ResponseCode.OK, gameList);
        } else {
            throw new GlobalException(ErrorCode.EXTERNAL_API_ERROR);
        }
    }

    private List<GameResponseDto> convertToGameResponseDtoList(Object apiResponseData) {
        if (!(apiResponseData instanceof List<?>)) {
            throw new GlobalException(ErrorCode.INVALID_API_RESPONSE);
        }

        List<?> responseDataList = (List<?>) apiResponseData;
        List<GameResponseDto> gameResponseDtos = new ArrayList<>();

        for (Object responseData : responseDataList) {
            Map<String, Object> gameData = (Map<String, Object>) responseData;
            gameResponseDtos.add(GameResponseDto.fromMap(gameData));
        }

        return gameResponseDtos;
    }

    private String determineApiUrl(String sportType) {
        switch (sportType.toLowerCase()) {
            case "football":
                return "https://api-football-v1.p.rapidapi.com/v3/timezone";
            case "basketball":
                return "https://api-basketball.p.rapidapi.com/timezone";
            case "baseball":
                return "https://api-baseball.p.rapidapi.com/timezone";
            default:
                throw new IllegalArgumentException("Invalid sport type");
        }
    }
}
