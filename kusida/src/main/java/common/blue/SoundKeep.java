package common.blue;


public class SoundKeep {
//    private static SoundKeep _instance;
//    public static SoundKeep getInstance() {
//        if (_instance == null)
//            _instance = new SoundKeep();
//        return _instance;
//    }
//    //=============================================================
//
//
//    public void stopMediaSoundKeep() {
//        if(mediaPlayerL!=null && mediaPlayerL.isPlaying()){
//            mediaPlayerL.pause();
//        }
//    }
//    private MediaPlayer mediaPlayerL;
//    public void playMediaSoundKeep(Context context) {
//        if (mediaPlayerL == null) {
//            mediaPlayerL = new MediaPlayer();
//            mediaPlayerL = MediaPlayer.create(context, R.raw.slience);
//            mediaPlayerL.setLooping(true);
//            mediaPlayerL.setVolume(0.1f, 0.1f);
//            mediaPlayerL.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayerL.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mp) {
//                    mediaPlayerL.seekTo(0);
//                }
//            });
//            mediaPlayerL.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mediaPlayerL.seekTo(0);
//                    mediaPlayerL.start();
//                }
//            });
//            mediaPlayerL.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//                @Override
//                public boolean onError(MediaPlayer mp, int what, int extra) {
//                    return false;
//                }
//            });
//            mediaPlayerL.start();
//        } else if (!mediaPlayerL.isPlaying()) {
//            mediaPlayerL.start();
//        }
//
//    }
}
