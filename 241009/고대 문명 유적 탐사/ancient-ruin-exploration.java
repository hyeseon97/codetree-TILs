import java.util.*;
import java.io.*;

public class Main {

    // 탐사 반복 횟수
    static int K;

    // 유물 조각 개수
    static int M;

    // 유물 조각 인덱스
    static int indexM;

    // 유물 조각 리스트
    static int[] mList;

    // 유적지
    static int[][] field;

    // 회전시킨 임시 유적지
    static int[][] tempField;

    // 유물 1차 획득에서 최대 가치를 가지는 회전각도와 좌표
    static int value;
    static int degree;
    static int maxI;
    static int maxJ;

    // 회전시키기 위한 방향 배열
    static int[] di = {-1, -1, -1, 0, 1, 1, 1, 0};
    static int[] dj = {-1, 0, 1, 1, 1, 0, -1, -1};

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        K = Integer.parseInt(st.nextToken());
        M = Integer.parseInt(st.nextToken());
        indexM = 0;
        field = new int[5][5];
        for(int i = 0;i<5;i++){
            st = new StringTokenizer(br.readLine());
            for(int j = 0;j<5;j++){
                field[i][j] = Integer.parseInt(st.nextToken());
            }
        }
        mList = new int[M];
        st = new StringTokenizer(br.readLine());
        for(int i = 0;i<M;i++){
            mList[i] = Integer.parseInt(st.nextToken());
        }
        // 입력 완료


        // K만큼 탐사 반복
        for(int k = 0;k<K;k++){
            
            // 5*5 배열에서 3*3을 선택할 수 있는 9가지 경우마다
            // 90도 회전, 180도 회전, 270도 회전시켜서 유물 1차 획득 가치를 최대화
            value = 0;
            degree = 1;
            for(int j = 1;j<=3;j++){
                for(int i = 1;i<=3;i++){
                    for(int r = 1;r<=3;r++){
                        rotation(i, j, r);
                        getFirst(i, j, r);
                    }
                }
            }   

            // 최대 가치가 0이면 종료
            if(value == 0) break;

            // 최대 가치를 가지는 경우로 유물 획득 -> 연쇄 획득 반복
            rotation(maxI, maxJ, degree);
            
            // 이번 탐사에서 얻는 총 가치
            int result = 0;
            
            while(true){
                int total = getValue();
                // 더이상 유물 획득할 수 없으면 종료
                if(total == 0) break;
                
                // 결과에 더하기
                result += total;

                // 비워진 칸에 유물 조각 채워넣기
                fill();

                // tempField를 진짜 field로 이동
                copy(field, tempField);
            }

            System.out.print(result + " ");
        }

    }

    // 유물 1차 획득
    static void getFirst(int i, int j, int r){

        int total = getValue();
        
        // 최대가치 갱신
        if(total > value){
            value = total;
            degree = r;
            maxI = i;
            maxJ = j;
        } else if(total == value){
            if(r < degree){
                degree = r;
                maxI = i;
                maxJ = j;
            } else if(degree == r){
                if(j < maxJ){
                    maxI = i;
                    maxJ = j;
                } else if(j == maxJ){
                    if(i < maxI){
                        maxI = i;
                    }
                }
            }
        }
    }

    // 가치 구하기
    static int getValue(){

        // 이번 유적지에서 얻을 수 있는 가치와 유물 좌표
        int total = 0;
        Stack<int[]> location = new Stack<>();

        //  방문체크
        boolean[][] visited = new boolean[5][5];

        // BFS로 모든 좌표에서 시작해 유물 찾기
        for(int i = 0;i<5;i++){
            for(int j = 0;j<5;j++){
                if(visited[i][j]) continue;
                // 이번 유물에서 시작하는 bfs
                Queue<int[]> q = new LinkedList<>();
                q.add(new int[]{i, j});
                location.push(new int[]{i, j});
                visited[i][j] = true;
                // 이번 유물의 인접한 조각 개수
                int count = 1;
                // 이번 유물 번호
                int num = tempField[i][j];

                while(!q.isEmpty()){
                    int[] temp = q.poll();
                    int r = temp[0];
                    int c = temp[1];

                    for(int d = 1;d<8;d+=2){
                        int nr = r+di[d];
                        int nc = c+dj[d];
                        if(nr<0 || nc<0 || nr>=5 || nc>=5 || visited[nr][nc] || tempField[nr][nc]!=num) continue;
                        q.add(new int[]{nr, nc});
                        location.push(new int[]{nr, nc});
                        visited[nr][nc] = true;
                        count++;
                    }
                }

                // 이번 인접 조각 개수가 3개 이상이면 total에 포함시키고 유적지에서 빼기
                // 아니면 그냥 location 비우기
                if(count >= 3){
                    total += count;
                    while(!location.isEmpty()){
                        int[] temp = location.pop();
                        tempField[temp[0]][temp[1]] = 0;
                    }
                } else{
                    while(!location.isEmpty()){
                        location.pop();
                    }
                }

            }

        }

        return total;
        
    }

    // 벽면에 써있는 숫자를 빈 유적지칸에 저장
    static void fill(){
        for(int j = 0;j<5;j++){
            for(int i = 4;i>=0;i--){
                if(tempField[i][j] == 0){
                    tempField[i][j] = mList[indexM++];
                }
            }
        }
    }

    // 중심좌표에서 회전
    static void rotation(int I, int J, int r){

        tempField = new int[5][5];

        // 90도 회전
        if(r == 1){
            for(int i = 0;i<5;i++){
                for(int j = 0;j<5;j++){
                    tempField[i][j] = field[i][j];
                }
            }
            for(int idx = 0;idx<8;idx++){
                tempField[I+di[idx]][J+dj[idx]] = field[I+di[(idx+6)%8]][J+dj[(idx+6)%8]];
            }
        }


        // 180도 회전
        if(r == 2){
            for(int i = 0;i<5;i++){
                for(int j = 0;j<5;j++){
                    tempField[i][j] = field[i][j];
                }
            }
            for(int idx = 0;idx<8;idx++){
                tempField[I+di[idx]][J+dj[idx]] = field[I+di[(idx+4)%8]][J+dj[(idx+4)%8]];
            }
        }

        // 270도 회전
        if(r == 3){
            for(int i = 0;i<5;i++){
                for(int j = 0;j<5;j++){
                    tempField[i][j] = field[i][j];
                }
            }
            for(int idx = 0;idx<8;idx++){
                tempField[I+di[idx]][J+dj[idx]] = field[I+di[(idx+2)%8]][J+dj[(idx+2)%8]];
            }
        }
        
    }

    // 유적지 복사
    static void copy(int[][] origin, int[][] temp){
        for(int i = 0;i<5;i++){
            for(int j = 0;j<5;j++){
                origin[i][j] = temp[i][j];
            }
        }
    }

    // 유적지 출력
    static void print(int[][] arr){
        for(int i = 0;i<5;i++){
            for(int j = 0;j<5;j++){
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}