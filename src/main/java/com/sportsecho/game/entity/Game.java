package com.sportsecho.game.entity;

import com.sportsecho.comment.entity.Comment;
import com.sportsecho.common.time.TimeStamp;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "game")
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Game extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String homeTeamName;
    private String homeTeamLogo;
    private String awayTeamName;
    private String awayTeamLogo;
    private String leagueLogo;
    private LocalDateTime date;
    private String venueName;
    private int homeGoal;
    private int awayGoal;

    @OneToMany(mappedBy = "game", fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    public static Game createGame(String homeTeamName, String homeTeamLogo, String awayTeamName,
        String awayTeamLogo, String leagueLogo, LocalDateTime date, String venueName, int homeGoal, int awayGoal) {
        return Game.builder()
            .homeTeamName(homeTeamName)
            .homeTeamLogo(homeTeamLogo)
            .awayTeamName(awayTeamName)
            .awayTeamLogo(awayTeamLogo)
            .leagueLogo(leagueLogo)
            .date(date)
            .venueName(venueName)
            .homeGoal(homeGoal)
            .awayGoal(awayGoal)
            .build();
    }

    public void updateGameScore(int homeGoal, int awayGoal) {
        this.homeGoal = homeGoal;
        this.awayGoal = awayGoal;
    }

}
