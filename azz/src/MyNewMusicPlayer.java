import javazoom.jl.player.Player;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

class MyNewMusicPlayer1 {

    static class JPlayer {
        private Player player;
        private Thread playingThread;
        private FileInputStream fileInputStream;
        private long pauseLocation;
        private long totalSongLength;
        private String currentSongPath;
        private boolean isPaused = false;

        private final List<String> songFilePaths;
        private final List<String> songNames;
        public JFrame mainFrame;

        public JPlayer(List<String> songFilePaths, List<String> songNames) {
            this.songFilePaths = new ArrayList<>(songFilePaths);
            this.songNames = new ArrayList<>(songNames);
            this.mainFrame = new JFrame("MP3 Player");
            initializeMainUI();
        }

        private void initializeMainUI() {
            mainFrame.setSize(400, 250);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setLayout(new BorderLayout());

            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new GridLayout(2,1,20,10));

            JButton playlistButton = new JButton("Open Playlist");
            playlistButton.addActionListener(e -> openPlaylistWindow());
            playlistButton.setFont(new Font("Times New Roman" , Font.BOLD , 20));

            JButton addSongButton = new JButton("Add Song");


            addSongButton.setFont(new Font("Times New Roman" , Font.BOLD , 20));
            addSongButton.addActionListener(e -> openAddSongWindow());
            controlPanel.add(playlistButton, BorderLayout.NORTH);
            controlPanel.add(addSongButton, BorderLayout.SOUTH);
            mainFrame.add(controlPanel, BorderLayout.CENTER);
            mainFrame.setVisible(true);
        }

        private void openAddSongWindow() {
            JFrame addSongFrame = new JFrame("Add Song");
            addSongFrame.setSize(300, 200);
            addSongFrame.setLayout(new GridLayout(3, 2));
            JOptionPane.showMessageDialog(null,
                    "enter the path correctly and please enter the path without double quotes otherwise it will not consider your path");

            JLabel nameLabel = new JLabel("Song Name:");
            JTextField nameField = new JTextField();
            JLabel pathLabel = new JLabel("Song Path:");
            JTextField pathField = new JTextField();
            JButton addButton = new JButton("Add");
            addButton.addActionListener(e -> {
                String songName = nameField.getText().trim();
                String songPath = pathField.getText().trim();

                if (!songName.isEmpty() && !songPath.isEmpty()) {
                    File file = new File(songPath);
                    if (file.exists()) {
                        songNames.add(songName);
                        songFilePaths.add(songPath);
                        JOptionPane.showMessageDialog(addSongFrame, "Song \"" + songName + "\" has been successfully added!", "Song Added", JOptionPane.INFORMATION_MESSAGE);
                        addSongFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(addSongFrame, "Invalid file path!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(addSongFrame, "Fields cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            addSongFrame.add(nameLabel);
            addSongFrame.add(nameField);
            addSongFrame.add(pathLabel);
            addSongFrame.add(pathField);
            addSongFrame.add(new JLabel());
            addSongFrame.add(addButton);

            addSongFrame.setVisible(true);
        }

        private void openPlaylistWindow() {
            JFrame playlistFrame = new JFrame("Playlist");
            playlistFrame.setSize(400, 400);
            playlistFrame.setLayout(new BorderLayout());

            JPanel playlistPanel = new JPanel();
            playlistPanel.setLayout(new GridLayout(0, 1));

            for (int i = 0; i < songFilePaths.size(); i++) {
                JButton playButton = new JButton(songNames.get(i));
                int index = i;
                playButton.addActionListener(e -> {
                    playMP3(songFilePaths.get(index));
                    showPlayerUI(index);
                });
                playlistPanel.add(playButton);
            }

            JScrollPane scrollPane = new JScrollPane(playlistPanel);
            playlistFrame.add(scrollPane, BorderLayout.CENTER);

            playlistFrame.setVisible(true);
        }

        public synchronized void playMP3(String filePath) {
            try {
                if (isPaused && filePath.equals(currentSongPath)) {
                    resumeSong();
                    return;
                }
                stopCurrentSong();

                File file = new File(filePath);
                if (!file.exists()) {
                    System.out.println("File does not exist: " + filePath);
                    return;
                }

                currentSongPath = filePath;
                fileInputStream = new FileInputStream(file);
                totalSongLength = fileInputStream.available();
                player = new Player(fileInputStream);

                playingThread = new Thread(() -> {
                    try {
                        System.out.println("Playing: " + file.getName());
                        player.play();
                        System.out.println("Playback completed.");
                    } catch (Exception e) {
                        System.out.println("Error during playback.");
                        e.printStackTrace();
                    }
                });
                playingThread.start();
                isPaused = false;
            } catch (Exception e) {
                System.out.println("Error initializing MP3 player.");
                e.printStackTrace();
            }
        }

        public synchronized void stopCurrentSong() {
            try {
                if (player != null) {
                    player.close();
                    System.out.println("Current song stopped.");
                }
                if (playingThread != null && playingThread.isAlive()) {
                    playingThread.interrupt();
                }
                isPaused = false;
            } catch (Exception e) {
                System.out.println("Error stopping current song.");
                e.printStackTrace();
            }
        }

        public synchronized void pauseSong() {
            try {
                if (player != null) {
                    pauseLocation = fileInputStream.available();
                    player.close();
                    isPaused = true;
                    System.out.println("Song paused.");
                }
            } catch (Exception e) {
                System.out.println("Error pausing song.");
                e.printStackTrace();
            }
        }

        public synchronized void resumeSong() {
            try {
                File file = new File(currentSongPath);
                fileInputStream = new FileInputStream(file);
                fileInputStream.skip(totalSongLength - pauseLocation);
                player = new Player(fileInputStream);

                playingThread = new Thread(() -> {
                    try {
                        System.out.println("Resuming: " + file.getName());
                        player.play();
                        System.out.println("Playback completed.");
                    } catch (Exception e) {
                        System.out.println("Error during playback.");
                        e.printStackTrace();
                    }
                });
                playingThread.start();
                isPaused = false;
            } catch (Exception e) {
                System.out.println("Error resuming song.");
                e.printStackTrace();
            }
        }

        public void showPlayerUI(int index) {
            JFrame playerFrame = new JFrame("Now Playing: " + songNames.get(index));
            playerFrame.setSize(470, 200);
            playerFrame.setLayout(new BorderLayout());

            JPanel labelPanel = new JPanel(new GridBagLayout());
            JLabel songLabel = new JLabel("Now Playing: " + songNames.get(index), SwingConstants.CENTER);
            songLabel.setFont(new Font("Times New Roman", Font.BOLD, 25));

            songLabel.setOpaque(true);
//			songLabel.setBackground(Color.LIGHT_GRAY); // Optional: for better visibility
            songLabel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            labelPanel.add(songLabel);

            playerFrame.add(labelPanel, BorderLayout.CENTER);

            JPanel controls = new JPanel();
            controls.setLayout(new GridLayout(1, 4));

            JButton playButton = new JButton("Play");
//			JButton stopButton = new JButton("Stop");
            JButton pauseButton = new JButton("Pause");
            JButton resumeButton = new JButton("Resume");
            JButton nextButton = new JButton("Next");
            JButton previousButton = new JButton("Previous");

            Dimension buttonSize = new Dimension(100, 40);
            playButton.setPreferredSize(buttonSize);
//			stopButton.setPreferredSize(buttonSize);
            pauseButton.setPreferredSize(buttonSize);
            resumeButton.setPreferredSize(buttonSize);
            nextButton.setPreferredSize(buttonSize);
            previousButton.setPreferredSize(buttonSize);

            controls.add(previousButton);
            controls.add(playButton);
//			controls.add(stopButton);
            controls.add(pauseButton);
            controls.add(resumeButton);
            controls.add(nextButton);

            final int finalIndex = index;
//			stopButton.addActionListener(e -> stopCurrentSong());
            playButton.addActionListener(e -> playMP3(songFilePaths.get(finalIndex)));
            pauseButton.addActionListener(e -> pauseSong());
            resumeButton.addActionListener(e -> resumeSong());
            nextButton.addActionListener(e -> {
                int nextIndex = (finalIndex + 1) % songFilePaths.size();
                playMP3(songFilePaths.get(nextIndex));
                playerFrame.dispose();
                showPlayerUI(nextIndex);
            });
            previousButton.addActionListener(e -> {
                int previousIndex = (finalIndex - 1 + songFilePaths.size()) % songFilePaths.size();
                playMP3(songFilePaths.get(previousIndex));
                playerFrame.dispose();
                showPlayerUI(previousIndex);
            });

            controls.add(previousButton);
            controls.add(playButton);
//			controls.add(stopButton);
            controls.add(pauseButton);
            controls.add(resumeButton);
            controls.add(nextButton);

            playerFrame.add(controls, BorderLayout.SOUTH);
//			playerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            playerFrame.setVisible(true);
        }

    }

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(
                null,
                "For listening Songs go to Playlist\n" +
                        "Otherwise, go to 'Add Song' to add songs manually.",
                "Welcome to Music Player",
                JOptionPane.INFORMATION_MESSAGE

        );

        List<String> songFilePaths = List.of(
                "C:\\Users\\DELL\\Downloads\\Jo Tum Mere Ho - Anuv Jain 128 Kbps.mp3",
                "C:\\Users\\DELL\\Downloads\\Russian Bandana - Dhanda Nyoliwala 128 Kbps.mp3",
                "C:\\Users\\DELL\\Downloads\\gta-san-andreas-6042.mp3"
                ,"C:\\Users\\DELL\\Downloads\\Nakhre-Zack-Knight (1).mp3",
                "C:\\Users\\DELL\\Downloads\\Guzarish Ghajini 128 Kbps.mp3"
        );

        List<String> songNames = List.of(
                "jo tum mere ho","russian bandana","gta","nakhre","guzarish"
        );
        new JPlayer(songFilePaths, songNames);
    }
}