public class Main {

    public static void main(String[] args) throws Exception {


        if(args.length == 0) return;
        

        if(args[0].equalsIgnoreCase("broker")) {

        } 
        else if(args[0].equalsIgnoreCase("producer")) {

        }
        else if(args[0].equalsIgnoreCase("consumer")) {

        }
        else {
            GetIp.getIp();
        }
        
    }
    
}
