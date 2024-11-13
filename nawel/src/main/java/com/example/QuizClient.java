package com.example;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import com.example.QuizOuterClass.Player;
import com.example.QuizOuterClass.Quiz;
import com.example.QuizOuterClass.QuizList;
import com.example.QuizOuterClass.PlayerList;
import com.google.protobuf.Empty;

public class QuizClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext()
                .build();

        QuizGameGrpc.QuizGameBlockingStub stub = QuizGameGrpc.newBlockingStub(channel);

        Player player = Player.newBuilder().setPlayerName("Player1").build();
        stub.registerPlayer(player);

        QuizList quizList = stub.getQuiz(Empty.newBuilder().build());
        for (Quiz quiz : quizList.getQuizzesList()) {
            System.out.println("Question: " + quiz.getQuestion());
            // Simulate answering the question
            Quiz answer = Quiz.newBuilder(quiz).setAnswer(1).build();
            Player updatedPlayer = stub.play(answer);
            System.out.println("Player score: " + updatedPlayer.getScore());
        }

        PlayerList playerList = stub.getPlayerScores(Empty.newBuilder().build());
        for (Player p : playerList.getPlayersList()) {
            System.out.println("Player: " + p.getPlayerName() + ", Score: " + p.getScore());
        }

        channel.shutdown();
    }
}