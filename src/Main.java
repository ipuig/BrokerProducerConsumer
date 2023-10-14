public class Main {

    private static Producer createProducer(String[] args) {
        if (args.length < 2) return new Producer(); // text
        if (args[1].equalsIgnoreCase("video")) return new Producer(1); // video
        return new Producer(2); // audio
    }

    public static void main(String[] args) throws Exception {


        if(args.length == 0) {
            GetIp.getIp();
            return;
        } 

        NNode svr;
        

        if(args[0].equalsIgnoreCase("broker")) {
            svr = new Broker();
        } 
        else if(args[0].equalsIgnoreCase("producer")) {
            svr = createProducer(args);

        }
        else {
            svr = new Consumer();
        }

        svr.start();
        
    }
    
}
