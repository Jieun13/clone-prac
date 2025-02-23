package me.jiny.prac220.service;

import lombok.RequiredArgsConstructor;
import me.jiny.prac220.domain.Hashtag;
import me.jiny.prac220.repository.HashtagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    private static final Pattern HASHTAG_PATTERN = Pattern.compile("#(\\w+)");

    public static List<String> extractHashtags(String content) {
        Matcher matcher = HASHTAG_PATTERN.matcher(content);
        return matcher.results()
                .map(match -> match.group(1)) // "#" 제거하고 단어만 추출
                .distinct() // 중복 제거
                .collect(Collectors.toList());
    }

    public Hashtag create(String keyword){
        return hashtagRepository.findByKeyword(keyword)
                .orElseGet(() -> hashtagRepository.save(new Hashtag(keyword)));
    }

    public Hashtag findByKeyword(String keyword){
        return hashtagRepository.findByKeyword(keyword).orElseThrow(()->new IllegalArgumentException("Hashtag not found"));
    }

    public List<Hashtag> findAll(){
        return hashtagRepository.findAll();
    }
}