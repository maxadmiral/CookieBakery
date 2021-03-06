import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

/**
 * For stuff to not get in the way
 * @author Max
 * @version 24.11.2017
 *
 */
abstract class Modifiers {
	
	public enum Type{
		ChordProgression,
		Riff,
		Solo
	}
	public enum Key{
		A,Am,Bb,Bbm,B,Bm,C,Cm,Db,Dbm,D,Dm,Eb,Ebm,E,Em,F,Fm,Gb,Gbm,G,Gm,Ab,Abm,Custom
	}
	
	static Random rand = new Random();
	int [][] notes;
	static Type type;
	Tab tab;
	
	//variables used for riff
	static int strings;	//number of strings
	static int frets;	//number of frets
	static int desiredNotes;	//number of notes to be included in the riff
	static int maxFretJump;	//maximum fret change between successive notes
	static int maxStringJump;	//maximum string change between successive notes
	static int maxDefStringJump;	//maximum defined string change between successive notes
	static Key key;	//current key
	int[] lastNote = {rand.nextInt(6),rand.nextInt(frets)};	//the latest note added to the tab
	static ArrayList <int[]> allNotes = new ArrayList<>();	//all notes in the riff
	int riffLength;	//number of notes in riff, if this exceeds maximum capacity of a tab row, a new one should be created
	static boolean smartNotePlacement;
	//
	//the notes in each key, remember that major uses the same notes as it's corresponding minor, e.g. C->Am
	int[][] AmNotes = {{0,0},{0,1},{0,3},{0,5},{0,7},{0,8},{0,10},{0,12},{0,13},{0,15},{0,17},{0,19},{0,20},{1,0},{1,1},{1,3},{1,5},{1,6},{1,8},{1,10},{1,12},{1,13},{1,15},{1,17},{1,18},{1,20},{2,0},{2,2},{2,4},{2,5},{2,7},{2,9},{2,10},{2,12},{2,14},{2,16},{2,17},{2,19},{3,0},{3,2},{3,3},{3,5},{3,7},{3,9},{3,10},{3,12},{3,14},{3,15},{3,17},{3,19},{4,0},{4,2},{4,3},{4,5},{4,7},{4,8},{4,10},{4,12},{4,14},{4,15},{4,17},{4,19},{4,20},{5,0},{5,1},{5,3},{5,5},{5,7},{5,8},{5,10},{5,12},{5,13},{5,15},{5,17},{5,19},{5,20}};
	int[][] BbmNotes = {{0,1},{0,2},{0,4},{0,6},{0,8},{0,9},{0,11},{0,13},{0,14},{0,16},{0,18},{0,20},{1,1},{1,2},{1,4},{1,6},{1,7},{1,9},{1,11},{1,13},{1,14},{1,16},{1,18},{1,19},{2,1},{2,3},{2,5},{2,6},{2,8},{2,10},{2,11},{2,13},{2,15},{2,17},{2,18},{2,20},{3,1},{3,3},{3,4},{3,6},{3,8},{3,10},{3,11},{3,13},{3,15},{3,16},{3,18},{3,20},{4,1},{4,3},{4,4},{4,6},{4,8},{4,9},{4,11},{4,13},{4,15},{4,16},{4,18},{4,20},{5,1},{5,2},{5,4},{5,6},{5,8},{5,9},{5,11},{5,13},{5,15},{5,16},{5,18},{5,20}};
	int[][] EmNotes = {{0,0},{0,2},{0,3},{0,5},{0,7},{0,8},{0,10},{0,12},{0,14},{0,15},{0,17},{0,19},{0,20},{1,0},{1,1},{1,3},{1,5},{1,7},{1,8},{1,10},{1,12},{1,13},{1,15},{1,17},{1,19},{1,20},{2,0},{2,2},{2,4},{2,5},{2,7},{2,9},{2,11},{2,12},{2,14},{2,16},{2,17},{2,19},{3,0},{3,2},{3,4},{3,5},{3,7},{3,9},{3,10},{3,12},{3,14},{3,16},{3,17},{3,19},{4,0},{4,2},{4,3},{4,5},{4,7},{4,9},{4,10},{4,12},{4,14},{4,15},{4,17},{4,19},{5,0},{5,2},{5,3},{5,5},{5,7},{5,8},{5,10},{5,12},{5,14},{5,15},{5,17},{5,19},{5,20}};
	int[][] FmNotes = {{0,1},{0,3},{0,4},{0,6},{0,8},{0,9},{0,11},{0,13},{0,15},{0,16},{0,18},{0,20},{1,1},{1,2},{1,4},{1,6},{1,8},{1,9},{1,11},{1,13},{1,14},{1,16},{1,18},{1,20},{2,0},{2,1},{2,3},{2,5},{2,6},{2,8},{2,10},{2,12},{2,13},{2,15},{2,17},{2,18},{2,20},{3,1},{3,3},{3,5},{3,6},{3,8},{3,10},{3,11},{3,13},{3,15},{3,17},{3,18},{3,20},{4,1},{4,3},{4,4},{4,6},{4,8},{4,10},{4,11},{4,13},{4,15},{4,16},{4,18},{4,20},{5,1},{5,3},{5,4},{5,6},{5,8},{5,9},{5,11},{5,13},{5,15},{5,16},{5,18},{5,20}};
	int[][] BmNotes = {{0,0},{0,2},{0,3},{0,5},{0,7},{0,9},{0,10},{0,12},{0,14},{0,15},{0,17},{0,19},{1,0},{1,2},{1,3},{1,5},{1,7},{1,8},{1,10},{1,12},{1,14},{1,15},{1,17},{1,19},{1,20},{2,0},{2,2},{2,4},{2,6},{2,7},{2,9},{2,11},{2,12},{2,14},{2,16},{2,18},{2,19},{3,0},{3,2},{3,4},{3,5},{3,7},{3,9},{3,11},{3,12},{3,14},{3,16},{3,17},{3,19},{4,0},{4,2},{4,4},{4,5},{4,7},{4,9},{4,10},{4,12},{4,14},{4,16},{4,17},{4,19},{5,0},{5,2},{5,3},{5,5},{5,7},{5,9},{5,10},{5,12},{5,14},{5,15},{5,17},{5,19}};
	int[][] CmNotes = {{0,1},{0,3},{0,4},{0,6},{0,8},{0,10},{0,11},{0,13},{0,15},{0,16},{0,18},{0,20},{1,1},{1,3},{1,4},{1,6},{1,8},{1,9},{1,11},{1,13},{1,15},{1,16},{1,18},{1,20},{2,1},{2,3},{2,5},{2,7},{2,8},{2,10},{2,12},{2,13},{2,15},{2,17},{2,19},{2,20},{3,1},{3,3},{3,5},{3,6},{3,8},{3,10},{3,12},{3,14},{3,15},{3,17},{3,18},{3,20},{4,1},{4,3},{4,5},{4,6},{4,8},{4,10},{4,11},{4,13},{4,15},{4,17},{4,18},{4,20},{5,1},{5,3},{5,4},{5,6},{5,8},{5,10},{5,11},{5,13},{5,15},{5,16},{5,18},{5,20}};
	int[][] DmNotes = {{0,0},{0,1},{0,3},{0,5},{0,6},{0,8},{0,10},{0,12},{0,13},{0,15},{0,17},{0,18},{0,20},{1,1},{1,3},{1,5},{1,6},{1,8},{1,10},{1,11},{1,13},{1,15},{1,17},{1,18},{1,20},{2,0},{2,2},{2,3},{2,5},{2,7},{2,9},{2,10},{2,12},{2,14},{2,15},{2,17},{2,19},{3,0},{3,2},{3,3},{3,5},{3,7},{3,8},{3,10},{3,12},{3,14},{3,15},{3,17},{3,19},{3,20},{4,0},{4,1},{4,3},{4,5},{4,7},{4,8},{4,10},{4,12},{4,13},{4,15},{4,17},{4,19},{4,20},{5,0},{5,1},{5,3},{5,5},{5,6},{5,8},{5,10},{5,12},{5,13},{5,15},{5,17},{5,18},{5,20}};
	//the root notes, used to create custom scales
	int[][] ARoots = { {0,5},{0,17},{1,10},{2,2},{2,14},{3,7},{3,19},{4,0},{4,12},{5,5},{5,17} };
	int[][] BbRoots = { {0,6},{0,18},{1,11},{2,3},{2,15},{3,8},{3,20},{4,1},{4,13},{5,6},{5,18} };
	int[][] BRoots = { {0,7},{0,19},{1,0},{1,12},{2,4},{2,16},{3,9},{4,2},{4,14},{5,7},{5,19} };
	int[][] CRoots = { {0,8},{0,20},{1,1},{1,13},{2,5},{2,17},{3,10},{4,3},{4,15},{5,8},{5,20} };
	int[][] DbRoots = { {0,9},{1,2},{1,14},{2,6},{2,18},{3,11},{4,4},{4,16},{5,9} };
	int[][] DRoots = { {0,10},{1,3},{1,15},{2,7},{2,19},{3,0},{3,12},{4,5},{4,17},{5,10} };
	int[][] EbRoots = { {0,11},{1,4},{1,16},{2,8},{2,20},{3,1},{3,13},{4,6},{4,18},{5,11} };
	int[][] ERoots = { {0,0},{0,12},{1,5},{1,17},{2,9},{3,2},{3,14},{4,7},{4,19},{5,0},{5,12} };
	int[][] FRoots = { {0,1},{0,13},{1,6},{1,18},{2,10},{3,3},{3,15},{4,8},{4,20},{5,1},{5,13} };
	int[][] GbRoots = { {0,2},{0,14},{1,7},{1,19},{2,11},{3,4},{3,16},{4,9},{5,2},{5,14} };
	int[][] GRoots = { {0,3},{0,15},{1,8},{1,20},{2,0},{2,12},{3,5},{3,17},{4,10},{5,3},{5,15} };
	int[][] AbRoots = { {0,4},{0,16},{1,9},{2,1},{2,13},{3,6},{3,18},{4,11},{5,4},{5,16} };
	//booleans determining which notes should be used in the custom scale
	static boolean A,Am,Bb,Bbm,B,Bm,C,Cm,Db,Dbm,D,Dm,Eb,Ebm,E,Em,F,Fm,Gb,Gbm,G,Gm,Ab,Abm;
	
	int[][] standardKeyNotes;	//used to determine currentKeyNotes if it is a normal predefined key
	ArrayList <int[]> currentKeyNotes = new ArrayList<>();;	//the notes in the current key
	ArrayList <int[]> posNewNotes = new ArrayList<>();	//possible new notes
	
	//variables used for chord progressions
	static int desiredChords;	//number of chords in the progression
	//different chord types
	String[] chordTypesFullMajor = {"","7","maj7","dim","dim7","aug","5","sus4","sus2","7sus4","7sus2","add2","add4","add9","6","6/9","9","maj9","11","maj11","13","maj13","7#9","7b9","7#5","7b5"};
	String[] chordTypesFullMinor = {"m","m7","m7b5","m/aug","5","m/maj7","sus4","sus2","7sus4","7sus2","m/add2","m/add4","m6","m9","m/maj9","m11","m/maj11","m13","m/maj13","m7#9","m7b9","m7#5"};
	String[] chordTypesExpandedMajor = {"","7","maj7","5","sus4","sus2"};
	String[] chordTypesExpandedMinor = {"m","m7","5","sus4","sus2"};
	String[] chordTypesBasicMajor1 = {"","maj7"};
	String[] chordTypesBasicMajor2 = {"","7"};
	String[] chordTypesBasicMinor = {"m","m7"};
	String[] chordTypesSimpleMajor = {""};
	String[] chordTypesSimpleMinor = {"m"};
	String[] chordTypesPower = {"5"};
	ArrayList <String> tonics = new ArrayList <String>(Arrays.asList("A","A#","B","C","C#","D","D#","E","F","F#","G","G#"));	//the root of the chord, pick one from these and type from the above
	String chordKey;
	ArrayList <String> rootNotes = new ArrayList<>();	//the possible root notes for the chords in the progression, i.e. do, re, mi...
	HashMap<String, String> minOrMaj = new HashMap<String, String>();
	
}
