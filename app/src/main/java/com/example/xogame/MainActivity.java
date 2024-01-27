package com.example.xogame;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private int grid_size;
    TableLayout gameBoard;
    TextView txt_turn;
    char [][] my_board;
    char turn;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grid_size = Integer.parseInt(getString(R.string.size_of_board));
        my_board = new char [grid_size][grid_size];
        gameBoard = findViewById(R.id.mainBoard);
        txt_turn = findViewById(R.id.turn);

        resetBoard();
        txt_turn.setText("Turn: "+turn);

        for(int i = 0; i< gameBoard.getChildCount(); i++){
            TableRow row = (TableRow) gameBoard.getChildAt(i);
            for(int j = 0; j<row.getChildCount(); j++){
                TextView tv = (TextView) row.getChildAt(j);
                tv.setText(R.string.none);
                tv.setOnClickListener(Move(i, j, tv));
            }
        }

        Button reset_btn = findViewById(R.id.reset);
        reset_btn.setOnClickListener(v -> {
            Intent current = getIntent();
            finish();
            startActivity(current);
        });
    }

    protected void resetBoard(){
        turn = 'X';
        for(int i = 0; i< grid_size; i++){
            for(int j = 0; j< grid_size; j++){
                my_board[i][j] = ' ';
            }
        }
    }

    protected int gameStatus(){

        //0 Continue
        //1 X Wins
        //2 O Wins
        //-1 Draw

        for(int i = 0; i< grid_size; i++){
            if(check_Row_Equality(i,'X'))
                return 1;
            if(check_Column_Equality(i, 'X'))
                return 1;
            if(check_Row_Equality(i,'O'))
                return 2;
            if(check_Column_Equality(i,'O'))
                return 2;
            if(check_Diagonal('X'))
                return 1;
            if(check_Diagonal('O'))
                return 2;
        }

        boolean boardFull = true;
        for(int i = 0; i< grid_size; i++){
            for(int j = 0; j< grid_size; j++){
                if (my_board[i][j] == ' ') {
                    boardFull = false;
                    break;
                }
            }
        }
        if(boardFull)
            return -1;
        else return 0;
    }

    protected boolean check_Diagonal(char player){
        int count_Equal1 = 0,count_Equal2 = 0;
        for(int i = 0; i< grid_size; i++)
            if(my_board[i][i]==player)
                count_Equal1++;
        for(int i = 0; i< grid_size; i++)
            if(my_board[i][grid_size -1-i]==player)
                count_Equal2++;
        return count_Equal1 == grid_size || count_Equal2 == grid_size;
    }

    protected boolean check_Row_Equality(int r, char player){
        int count_Equal=0;
        for(int i = 0; i< grid_size; i++){
            if(my_board[r][i]==player)
                count_Equal++;
        }

        return count_Equal == grid_size;
    }

    protected boolean check_Column_Equality(int c, char player){
        int count_Equal=0;
        for(int i = 0; i< grid_size; i++){
            if(my_board[i][c]==player)
                count_Equal++;
        }

        return count_Equal == grid_size;
    }

    protected boolean Cell_Set(int r, int c){
        return !(my_board[r][c]==' ');
    }

    protected void stopMatch(){
        for(int i = 0; i< gameBoard.getChildCount(); i++){
            TableRow row = (TableRow) gameBoard.getChildAt(i);
            for(int j = 0; j<row.getChildCount(); j++){
                TextView tv = (TextView) row.getChildAt(j);
                tv.setOnClickListener(null);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    View.OnClickListener Move(final int r, final int c, final TextView tv){

        return v -> {

            if(!Cell_Set(r,c)) {
                my_board[r][c] = turn;
                if (turn == 'X') {
                    tv.setText(R.string.X);
                    turn = 'O';
                } else if (turn == 'O') {
                    tv.setText(R.string.O);
                    turn = 'X';
                }
                if (gameStatus() == 0) {
                    txt_turn.setText("Turn: Player " + turn);
                }
                else if(gameStatus() == -1){
                    txt_turn.setText("This is a Draw match");
                    stopMatch();
                }
                else{
                    txt_turn.setText(turn+" Loses!");
                    stopMatch();
                }
            }
            else{
                txt_turn.setText(txt_turn.getText()+" Choose an Empty Call");
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_board, menu);
        return true;
    }


}