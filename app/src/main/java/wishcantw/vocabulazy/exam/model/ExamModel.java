package wishcantw.vocabulazy.exam.model;

import wishcantw.vocabulazy.storage.Database;
import wishcantw.vocabulazy.storage.databaseObjects.Vocabulary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by swallow on 2016/1/17.
 */
public class ExamModel {

    public static final String TAG = ExamModel.class.getSimpleName();

    private Database mDatabase;

    public ExamModel(){
        mDatabase = Database.getInstance();
    }

    public PuzzleSetter createPuzzleSetter(int bookIndex, int lessonIndex) {
        ArrayList<Vocabulary> vocabularies = mDatabase.getVocabulariesByIDs(mDatabase.getContentIds(bookIndex, lessonIndex));
        return new PuzzleSetter(vocabularies);
    }

    public class PuzzleSetter {

        private int mCurrentQuestionIndex;
        private int mTotalQuestionNum;
        private int mAnswerOptionIndex;
        private int mCorrectCount;

        private ArrayList<Vocabulary> mVocabularies;

        public PuzzleSetter(ArrayList<Vocabulary> vocabularies) {
            mVocabularies = shuffleArrayList(vocabularies);
            mCurrentQuestionIndex = -1;
            mTotalQuestionNum = mVocabularies.size();
            mCorrectCount = 0;
        }

        public HashMap<Integer, ArrayList<String>> getANewQuestion(){
            /***
             * HashMap<Integer, ArrayList<String>>
             *     KEY is QuestionAndOption, 0 means Question, 1-4 means option
             *     VALUE is english and translation
             */

            // Null map means not enough question to start exam
            if(mTotalQuestionNum < 5) {
                return null;
            }

            mCurrentQuestionIndex++;

            // If current question index >= total question number, the exam is over. Return empty map
            HashMap<Integer, ArrayList<String>> questionMap = new HashMap<>();
            if (mCurrentQuestionIndex >= mTotalQuestionNum) {
                return questionMap;
            }

            mAnswerOptionIndex = 0;     // reset the answer_option index
            do{     // Determine which option will be the answer
                mAnswerOptionIndex = new Random().nextInt(5);
            } while(mAnswerOptionIndex == 0);

            for(int i = 0; i < 5; i++){
                int pickIndex;      // It's used to pick up option from vocabularies
                ArrayList<String> strArr = new ArrayList<>();   //save option and answer string
                if(i == 0) {          // 0 is for question, only spell is needed
                    pickIndex = mCurrentQuestionIndex;
                    strArr.add(mVocabularies.get(pickIndex).getSpell());
                }
                else if(i == mAnswerOptionIndex){
                    pickIndex = mCurrentQuestionIndex;
                    strArr.add("");
                    strArr.add(mVocabularies.get(pickIndex).getTranslation());
                }
                else {
                    do {
                        pickIndex = new Random().nextInt(mTotalQuestionNum);
                    } while(pickIndex == mCurrentQuestionIndex);
                    strArr.add(mVocabularies.get(pickIndex).getSpell());
                    strArr.add(mVocabularies.get(pickIndex).getTranslation());
                }
                questionMap.put(i, strArr);
            }

            return questionMap;
        }

        public int checkAnswer(int checkIndex){
            if(checkIndex == mAnswerOptionIndex)
                mCorrectCount++;
            return mAnswerOptionIndex;
        }

        private ArrayList<Vocabulary> shuffleArrayList(ArrayList<Vocabulary> oldArrayList){
            ArrayList<Vocabulary> newArrayList = new ArrayList<>();
            Random rnd = new Random();
            for(int i = oldArrayList.size() - 1; i >= 0; i--) {
                int index = rnd.nextInt(i + 1);
                newArrayList.add(oldArrayList.get(index));
                oldArrayList.remove(index);
            }
            return newArrayList;
        }

        public int getCurrentQuestionIndex(){
            return mCurrentQuestionIndex + 1;       // because we can't show 00 to students
        }

        public int getTotalQuestionNum(){
            return mTotalQuestionNum;
        }

        public int getCorrectCount() { return mCorrectCount; }
    }


}
