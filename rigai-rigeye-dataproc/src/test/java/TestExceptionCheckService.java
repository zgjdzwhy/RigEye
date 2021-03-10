import java.util.regex.Pattern;

/**
 * @author wuhuiyong
 * @create 2018-09-29 17:11
 **/
public class TestExceptionCheckService {
    public static void main(String[] arg){
        String AbnormalContent="test[12345]\\|[0-9]\\|[0-9]*\\.?[0-9]+\\|([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8])))\\|info";
        String line="test1|7|2.6|2018-09-29|info";
        if (Pattern.matches(AbnormalContent,line)){
            System.out.println("正则匹配成功");
        }else{
            System.out.println("正则未匹配");
        }

    }
}
