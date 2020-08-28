import java.io.*;
import java.util.*;

public class Solution {
    //要查询的路径
    private static String filePath = "E:\\查询";

    private String outputFile = "E:\\查询\\result.txt";
    private HashMap<String, Integer> map = new LinkedHashMap<>();
    private static int len = 20;
    private static BloomFilter[] bloomFilters = new BloomFilter[len];
    private static List<String>[] list=new LinkedList[len];

    //布隆过滤器,双向链表初始化
    public static void init() throws Exception {
        for (int i = 0; i < len; i++) {
            if(list[i]==null){
                list[i]=new LinkedList<>();
            }
            if(bloomFilters[i]==null){
                bloomFilters[i]=new BloomFilter();
            }
            FileInputStream reader = new FileInputStream(filePath + "\\a" + i + ".txt");
            BufferedReader bf = new BufferedReader(new InputStreamReader(reader));
            String line = null;
            while ((line = bf.readLine()) != null) {
                list[i].add(line);
                boolean bl = bloomFilters[i].contains(line);
                if (!bl) {
                    bloomFilters[i].add(line);
                }
            }
            bf.close();
            reader.close();
        }
    }

    public void find(String line, int k) throws Exception {
        FileOutputStream writer = new FileOutputStream(outputFile);
        List<String> ans=new ArrayList<>();
        int index=0;
        for (int i = 0; i < len; i++) {
            if (bloomFilters[i].contains(line)) {
                for(int j=0;j<list[i].size();j++){
                    if(list[i].get(j).equals(line)){
                        int cnt=0;
                        int tmp_k=k;
                        if(j-tmp_k<0){
                            cnt++;
                            tmp_k--;
                        }
                        if(i>0) {//前一个文件
                            int size = list[i - 1].size();
                            while (cnt>0) {
                                ans.add(list[i - 1].get(size - cnt));
                                cnt--;
                            }
                        }
                        for(int t=tmp_k;t>=1;t--){
                            ans.add(list[i].get(j - t));
                        }
                        ans.add(list[i].get(j));
                        int t=1;
                        for(t=1;t<=k;t++){
                            if(j+t<list[i].size()) {
                                ans.add(list[i].get(j + t));
                            }else break;
                        }
                        if(j+t==list[i].size()){//后一个文件
                            int cur=0;
                            while (t<=k) {
                                ans.add(list[i + 1].get(cur++));
                                t++;
                            }
                        }
                    }
                }
                writer.write(("第"+i+"份文件：\n").getBytes());
                while (index<ans.size()) {
                    writer.write((ans.get(index++)+"\n").getBytes());
                }
                writer.write(("------------------------\n").getBytes());
            }
        }
        writer.close();
    }

}
