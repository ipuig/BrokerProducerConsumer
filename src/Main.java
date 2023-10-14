public class Main {

    private static Producer createProducer(String[] args) {
        if (args.length < 2) return new Producer(); 
        if (args[1].equalsIgnoreCase("video")) return new Producer(NNode.PRODUCER_TYPE.VIDEO_STREAMER);
        return new Producer(NNode.PRODUCER_TYPE.AUDIO_STREAMER);
    }

    public static void main(String[] args) throws Exception {

        if(args.length == 0) {
            GetIp.getIp();
            return;
        } 

        NNode svr;

        if(args[0].equalsIgnoreCase("broker")) svr = new Broker();
        else if(args[0].equalsIgnoreCase("producer")) svr = createProducer(args);
        else svr = new Consumer();

        svr.start();
    }
}
