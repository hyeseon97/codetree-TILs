import java.io.*;
import java.util.*;

public class Main {

    // 숲의 크기
    static int R;
    static int C;

    // 정령 수 
    static int K;

    // 정령 정보
    static int[][] fairy;

    // 골렘 클래스
    static class Golem{
        // 골렘 번호
        int num;
        // 골렘의 좌표
        int r;
        int c;
        // 골렘의 방향
        int d;

        void setter(int r, int c, int d){
            this.r = r;
            this.c = c;
            this.d = d;
        }

        Golem(int num, int r, int c, int d){
            this.num = num;
            this.r = r;
            this.c = c;
            this.d = d;
        }
    }

    // 숲 현황
    static Golem[][] forest;

    // 현재 내려가고 있는 골렘
    static Golem golem;

    // 정령이 위치한 행 총합
    static int total;

    // 4방향 배열
    static int[] dr = {-1, 0, 1, 0, 0};
    static int[] dc = {0, 1, 0, -1, 0};


    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        R = Integer.parseInt(st.nextToken());
        C = Integer.parseInt(st.nextToken());
        K = Integer.parseInt(st.nextToken());

        fairy = new int[K][2];
        for(int i = 0;i<K;i++){
            st = new StringTokenizer(br.readLine());
            fairy[i][0] = Integer.parseInt(st.nextToken());
            fairy[i][1] = Integer.parseInt(st.nextToken());
        }

        forest = new Golem[R+1][C+1];
        total = 0;
        
        // 정령 순회
        for(int i = 0;i<K;i++){

            // 현재 정령을 골렘에 태워 저장
            golem = new Golem(i, -1, fairy[i][0], fairy[i][1]);

            // 1. 골렘을 남쪽으로 이동하기
            boolean escape = golemMove();

            // down에서 false가 반환되었다는 것은 정령이 탈출하기 못하는 것으로 숲 초기화
            if(!escape){
                forest = new Golem[R+1][C+1];
                continue;
            }

            // 2. 정령이 가장 남쪽의 칸으로 이동
            int rowNumber = fairyMove();
            total += rowNumber;


        }

        System.out.println(total);


    }

    // 골렘을 남쪽으로 이동시키는 메서드
    static boolean golemMove(){

        while(true){

            // 1) 현재 골렘에서 한 칸 아래의 4방향에 아무것도 없으면 갈 수 있음
            if(move1()){
                golem.setter(golem.r+1, golem.c, golem.d);
            }

            // 2) 현재 골렘에서 왼쪽과 왼쪽 아래의 4방향에 아무것도 없으면 갈 수 있음
            else if(move2()){
                golem.setter(golem.r+1, golem.c-1, (golem.d-1==-1?3:golem.d-1));
            }
                
            // 3) 현재 골렘에서 오른쪽과 오른쪽 아래의 4방향에 아무것도 없으면 갈 수 있음
            else if(move3()){
                golem.setter(golem.r+1, golem.c+1, (golem.d+1)%4);
            }

            // 더이상 갈 수 없으면 while문 종료
            else {
                break;
            }
        }

        // 현재 골렘 몸의 일부가 숲에 들어오지 못했을 때 탈출하지 못하므로 false 반환
        if(golem.r < 2) return false;

        // 탈출할 수 있으면 골렘을 forest에 저장하고 true 반환
        for(int d = 0;d<5;d++){
            int nr = golem.r + dr[d];
            int nc = golem.c + dc[d];
            forest[nr][nc] = new Golem(golem.num, golem.r, golem.c, golem.d);
        }

        return true;
    }

    // 아래로 내려가는 이동 가능 여부 판단
    static boolean move1(){
        
        // 4방향 탐색했는데 갈 수 있으면 true 반환
        // 한 방향이라도 안되면 false 반환
        for(int d = 0;d<4;d++){
            int nr = golem.r+1 + dr[d];
            int nc = golem.c + dc[d];
            if(nc<=0 || nr>R || nc>C || (nr>0 && forest[nr][nc] != null)) return false;
        }

        return true;
    }

    // 왼쪽으로 회전해서 내려가는 이동 가능 여부 판단
    static boolean move2(){
        
        // 왼쪽과 왼쪽아래를 기준으로
        // 4방향 탐색했는데 갈 수 있으면 true 반환
        // 한 방향이라도 안되면 false 반환
        for(int d = 0;d<4;d++){
            int nr = golem.r + dr[d];
            int nc = golem.c-1 + dc[d];
            if(nc<=0 || nr>R || nc>C || (nr>0 && forest[nr][nc] != null)) return false;
        }

        for(int d = 0;d<4;d++){
            int nr = golem.r+1 + dr[d];
            int nc = golem.c-1 + dc[d];
            if(nc<=0 || nr>R || nc>C || (nr>0 && forest[nr][nc] != null)) return false;
        }

        return true;
    }

    // 오른쪽으로 회전해서 내려가는 이동 가능 여부 판단
    static boolean move3(){
        
        // 오른쪽과 오른쪽아래를 기준으로
        // 4방향 탐색했는데 갈 수 있으면 true 반환
        // 한 방향이라도 안되면 false 반환
        for(int d = 0;d<4;d++){
            int nr = golem.r + dr[d];
            int nc = golem.c+1 + dc[d];
            if(nc<=0 || nr>R || nc>C || (nr>0 && forest[nr][nc] != null)) return false;
        }

        for(int d = 0;d<4;d++){
            int nr = golem.r+1 + dr[d];
            int nc = golem.c+1 + dc[d];
            if(nc<=0 || nr>R || nc>C || (nr>0 && forest[nr][nc] != null)) return false;
        }

        return true;
    }

    // 정령을 가장 남쪽으로 이동시키는 메서드
    static int fairyMove(){

        // 현재 골렘의 출구에서 이동할 수 있는 골렘으로 BFS
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{golem.num, golem.r, golem.c});
        Set<Integer> visited = new HashSet<>();
        visited.add(golem.num);

        // 가장 남쪽으로 이동할 수 있는 행 번호 갱신
        int southR = 0;

        while(!q.isEmpty()){
            int[] temp = q.poll();
            int num = temp[0];
            int r = temp[1];
            int c = temp[2];

            // 현재 위치한 골렘의 남쪽 칸으로 행 번호 갱신
            southR = Math.max(southR, r+1);

            // 현재 위치한 골렘의 출구 칸 좌표
            int escapeR = r + dr[forest[r][c].d];
            int escapeC = c + dc[forest[r][c].d];
            
            // 출구 좌표의 4방향에 다른 골렘이 있으면서 방문하지 않은 골렘이면 이동
            for(int d = 0;d<4;d++){
                int nr = escapeR + dr[d];
                int nc = escapeC + dc[d];
                if((nr>0 && nc>0 && nr<=R && nc<=C) && forest[nr][nc] != null && !visited.contains(forest[nr][nc].num)){
                    q.add(new int[]{forest[nr][nc].num, forest[nr][nc].r, forest[nr][nc].c});
                    visited.add(forest[nr][nc].num);
                }
            }

        }

        return southR;
    }
}