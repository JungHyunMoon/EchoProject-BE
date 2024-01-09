package com.sportsecho.common.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportsecho.common.jwt.JwtUtil;
import com.sportsecho.global.exception.GlobalException;
import com.sportsecho.member.entity.Member;
import com.sportsecho.member.entity.MemberRole;
import com.sportsecho.member.entity.SocialType;
import com.sportsecho.member.exception.MemberErrorCode;
import com.sportsecho.member.repository.MemberRepository;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * kakaoOAuthDocs: https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token
 * naverOAuthDocs: https://developers.naver.com/docs/login/devguide/devguide.md
 * */

@Service
@Slf4j(topic = "OAUthUtil")
@RequiredArgsConstructor
public class OAuthUtil {

    @Value("${kakao-api-key}")
    private String kakaoApiKey;

    @Value("${naver-api-key}")
    private String naverApiKey;

    @Value("${naver-api-secret}")
    private String naverApiSecret;

    @Value("${google-api-key}")
    private String googleApiKey;

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;

    public String getToken(URI uri, SocialType socialType, String code) {
        try {
            // HTTP Header 생성
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(generateBody(socialType, code));

            // HTTP 요청 보내기
            ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
            );

            // HTTP 응답 (JSON) -> 액세스 토큰 파싱
            JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
            return jsonNode.get("access_token").asText();
        } catch (JsonProcessingException e) {

            //Error코드 추가하고 바꿀것
            throw new GlobalException(MemberErrorCode.DUPLICATED_EMAIL);
        }
    }

    public JsonNode getMemberInfo(URI uri, String accessToken) {
        try {
            // HTTP Header 생성
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + accessToken);
            headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

            // HTTP 요청 보내기
            ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
            );

            return new ObjectMapper().readTree(response.getBody());
        } catch(JsonProcessingException e) {

            //Error코드 추가하고 바꿀것
            throw new GlobalException(MemberErrorCode.DUPLICATED_EMAIL);
        }
    }

    public Member registerSocialMemberIfNeeded(Long socialId, String memberName, String email, SocialType socialType) {
        Member socialMember = memberRepository.findBySocialIdAndSocialType(socialId, socialType).orElse(null);

        if (socialMember == null) {
            // 카카오 사용자 email과 동일한 email 가진 회원이 있는지 확인
            Member sameEmailMember = memberRepository.findByEmail(email).orElse(null);

            if (sameEmailMember != null) {
                socialMember = sameEmailMember;
            } else {
                String encodedPassword = passwordEncoder.encode(UUID.randomUUID().toString());

                socialMember = Member.builder()
                    .memberName(memberName)
                    .email(email)
                    .password(encodedPassword)
                    .role(MemberRole.CUSTOMER)
                    .build();
            }

            //KakaoId update 및 저장
            socialMember = socialMember.updateSocialIdAndType(socialId, socialType);
            memberRepository.save(socialMember);
        }

        return socialMember;
    }

    private MultiValueMap<String, String> generateBody(SocialType socialType, String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        if(SocialType.KAKAO.equals(socialType)) {
            body.add("grant_type", "authorization_code");
            body.add("client_id", kakaoApiKey);
            body.add("redirect_uri", "http://localhost:8080/api/members/kakao/callback");
            body.add("code", code);
        }
        if(SocialType.NAVER.equals(socialType)) {
            body.add("grant_type", "authorization_code");
            body.add("client_id", naverApiKey);
            body.add("client_secret", naverApiSecret);
            body.add("code", code);
            body.add("state", "9kgsGTfH4j7IyAkg");
        }

        return body;
    }
}
