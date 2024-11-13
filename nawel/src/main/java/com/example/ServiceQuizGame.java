package com.example;

import io.grpc.stub.StreamObserver;
import com.example.QuizOuterClass.Empty;
import com.example.QuizOuterClass.Player;
import com.example.QuizOuterClass.Quiz;
import com.example.QuizOuterClass.QuizList;
import com.example.QuizOuterClass.PlayerList;
import java.util.ArrayList;
import java.util.List;

public class ServiceQuizGame extends QuizGameGrpc.QuizGameImplBase {
    private List<Quiz> quizzes = new ArrayList<>();
    private List<Player> players = new ArrayList<>();

    @Override
    public void registerPlayer(Player request, StreamObserver<Player> responseObserver) {
        players.add(request);
        responseObserver.onNext(request);
        responseObserver.onCompleted();
    }

    @Override
    public void getQuiz(Empty request, StreamObserver<QuizList> responseObserver) {
        QuizList quizList = QuizList.newBuilder().addAllQuizzes(quizzes).build();
        responseObserver.onNext(quizList);
        responseObserver.onCompleted();
    }

    @Override
    public void getQuestion(Quiz request, StreamObserver<Quiz> responseObserver) {
        for (Quiz quiz : quizzes) {
            if (quiz.getId() == request.getId()) {
                responseObserver.onNext(quiz);
                responseObserver.onCompleted();
                return;
            }
        }
        responseObserver.onError(new Exception("Quiz not found"));
    }

    @Override
    public void play(Quiz request, StreamObserver<Player> responseObserver) {
        for (Player player : players) {
            if (player.getPlayerName().equals(request.getPlayerName())) {
                if (request.getCorrectAnswer() == request.getAnswer()) {
                    player.setScore(player.getScore() + 1);
                }
                responseObserver.onNext(player);
                responseObserver.onCompleted();
                return;
            }
        }
        responseObserver.onError(new Exception("Player not found"));
    }

    @Override
    public void getPlayerScores(Empty request, StreamObserver<PlayerList> responseObserver) {
        PlayerList playerList = PlayerList.newBuilder().addAllPlayers(players).build();
        responseObserver.onNext(playerList);
        responseObserver.onCompleted();
    }
}