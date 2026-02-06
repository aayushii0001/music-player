package example.com;

import java.awt.BorderLayout;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.Timer;

class Project implements ActionListener {
	
	JFrame frame = new JFrame("Music Player");
	
	String fullSongName = "Now Playing: watch.wav";
	Timer scrollTimer;
	int scrollIndex = 0;

	
	JSlider progressBar; 
    Timer timer; 
	boolean isDragging = false;
	
	Border border = BorderFactory.createLineBorder(Color.WHITE, 5);
	
	File file = new File("watch.wav");
    AudioInputStream audio = AudioSystem.getAudioInputStream(file);
    Clip clip = AudioSystem.getClip();
    
    Scanner scanner = new Scanner(System.in); 
    
    JLabel label = new JLabel();
    
    JLabel songNameLabel = new JLabel("Now Playing: watch.wav");

    
    JLabel timeLabel = new JLabel("00:00 / 00:00");

    
    JLabel playtext = new JLabel("PLAY");
    JLabel pausetext = new JLabel("PAUSE");
    JLabel replaytext = new JLabel("REPLAY");
    ImageIcon witch = new ImageIcon("witch.png");
    
    JButton importBtn = new JButton("IMPORT");
 	
	JButton play = new JButton();
	JButton pause = new JButton();
	JButton replay = new JButton();
	
	ImageIcon playpng = new ImageIcon("play.png");
	ImageIcon pausepng = new ImageIcon("pause.png");
	ImageIcon replaypng = new ImageIcon("replay.png");
	
	Project() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
	
	frame.add(label);
	frame.setSize(750,750);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setResizable(false);
	
	importBtn.setBounds(20, 20, 100, 50);
	importBtn.addActionListener(this);
	importBtn.setForeground(Color.GRAY);

	songNameLabel.setBounds(480, 0, 700, 30); 
	songNameLabel.setForeground(Color.WHITE);
	songNameLabel.setFont(new Font("Times New Roman", Font.BOLD, 20));
	songNameLabel.setText(fullSongName);
	frame.add(songNameLabel);
	startScrollingText(); 


	frame.add(songNameLabel);

	frame.add(importBtn);
	
    frame.getRootPane().setBorder(border);
	
	ImageIcon image = new ImageIcon("titleplay.png");
	frame.setIconImage(image.getImage());
	frame.getContentPane().setBackground(Color.black);
	
	label.setIcon(witch);
	label.setBounds(-30, 0, 750, 750); // Full frame size

    clip.open(audio);
    
    timeLabel.setForeground(Color.WHITE);
    timeLabel.setFont(new Font("Consolas", Font.BOLD, 16));
    timeLabel.setHorizontalAlignment(JLabel.CENTER);
    timeLabel.setBounds(580, 660, 150, 30); // Near the progress bar
    frame.add(timeLabel);

     
    
    int songLength = (int) (clip.getMicrosecondLength() / 1000); // Convert to milliseconds
    progressBar = new JSlider(0, songLength, 0);
    progressBar.setMajorTickSpacing(songLength / 10);
    progressBar.setPaintTicks(true);
    progressBar.setPaintLabels(false);

    
    // Seek functionality
    progressBar.addChangeListener(new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
            if (isDragging) {
                clip.setMicrosecondPosition(progressBar.getValue() * 1000L);
            }
        }
    });

    progressBar.addMouseListener(new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            isDragging = true;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            isDragging = false;
            clip.setMicrosecondPosition(progressBar.getValue() * 1000L);
        }
    });

    frame.setLayout(null);
    progressBar.setBounds(0, 690, 750, 20); // Adjust as needed
    frame.add(progressBar);
	
	play.setBounds(150, 620, 100, 50);
	pause.setBounds(300, 620, 100, 50);
	replay.setBounds(450, 620, 100, 50);
	
	play.addActionListener(this);
	pause.addActionListener(this);
	replay.addActionListener(this);
	
	playtext.setBounds(185, 590, 40, 40);
	pausetext.setBounds(330, 590, 40, 40);
	replaytext.setBounds(475, 590, 50, 40);
	
	Font custom = new Font("Times New Roman", Font.BOLD, 13);
	
	playtext.setFont(custom);
	pausetext.setFont(custom);
	replaytext.setFont(custom);
	
	playtext.setForeground(Color.WHITE);
	pausetext.setForeground(Color.WHITE);
	replaytext.setForeground(Color.WHITE);
	
	frame.add(playtext);
	frame.add(pausetext);
	frame.add(replaytext);
	
	play.setIcon(playpng);
	pause.setIcon(pausepng);
	replay.setIcon(replaypng);
	
	frame.add(play);
	frame.add(pause);
	frame.add(replay);
	
	
	frame.setVisible(true);
	timer = new Timer(100, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (clip.isActive() && !isDragging) {
            	int currentMs = (int) (clip.getMicrosecondPosition() / 1000);
            	progressBar.setValue(currentMs);
            	timeLabel.setText(formatTime(currentMs) + " / " + formatTime(progressBar.getMaximum()));

            }
        }
    });
	
	frame.getContentPane().setComponentZOrder(label, frame.getContentPane().getComponentCount() - 1);

	

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==play) {
			clip.start();
			timer.start();
		}
		if(e.getSource()==pause) {
			clip.stop();
		}
		if(e.getSource()==replay) {
			clip.setMicrosecondPosition(0);
			clip.start();
			timer.start();
		}
		
		if (e.getSource() == importBtn) {
		    JFileChooser fileChooser = new JFileChooser();
		    fileChooser.setDialogTitle("Select a WAV File");
		    fileChooser.setAcceptAllFileFilterUsed(false);
		    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("WAV Sound Files", "wav"));

		    int result = fileChooser.showOpenDialog(null);
		    if (result == JFileChooser.APPROVE_OPTION) {
		        File selectedFile = fileChooser.getSelectedFile();
		        try {
		            // Stop current playback
		            if (clip.isOpen()) {
		                clip.stop();
		                clip.close();
		            }

		            // Load new audio
		            AudioInputStream newAudio = AudioSystem.getAudioInputStream(selectedFile);
		            clip.open(newAudio);

		            // Update song name
		            fullSongName = "Now Playing: " + selectedFile.getName();
		            startScrollingText();


		            // Reset and update progress bar
		            int songLength = (int) (clip.getMicrosecondLength() / 1000);
		            progressBar.setMaximum(songLength);
		            progressBar.setValue(0);

		            // Start playing
		            clip.setMicrosecondPosition(0);
		            clip.start();
		            timer.start();
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		    }
		}

		    
		}

		

	private String formatTime(int milliseconds) {
	    int totalSeconds = milliseconds / 1000;
	    int minutes = totalSeconds / 60;
	    int seconds = totalSeconds % 60;
	    return String.format("%02d:%02d", minutes, seconds);
	}
	private void startScrollingText() {
	    if (scrollTimer != null && scrollTimer.isRunning()) {
	        scrollTimer.stop();
	    }

	    scrollTimer = new Timer(150, new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            if (fullSongName.length() > 0) {
	                scrollIndex = (scrollIndex + 1) % fullSongName.length();
	                String scrollingText = fullSongName.substring(scrollIndex) + "   " + fullSongName.substring(0, scrollIndex);
	                songNameLabel.setText(scrollingText);
	            }
	        }
	    });
	    scrollTimer.start();
	}


}
