package com.hoehn.game.repository;

import com.hoehn.game.entities.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Integer> {

    // Retrieves the top 10 scores in descending order from the database
    List<Score> findTop10ByOrderByScoreDesc();

    // Retrieves the top 10 scores from a user in descending order from the database
    List<Score> findTop10ByUser_UserNameOrderByScoreDesc(String userName);

}
