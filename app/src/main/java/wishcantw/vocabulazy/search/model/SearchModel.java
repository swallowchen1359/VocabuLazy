package wishcantw.vocabulazy.search.model;

import wishcantw.vocabulazy.search.view.SearchDetailView;
import wishcantw.vocabulazy.search.view.SearchListView;
import wishcantw.vocabulazy.storage.Database;
import wishcantw.vocabulazy.storage.databaseObjects.Vocabulary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class SearchModel {

    private Database mDatabase;

    public SearchModel() {
        mDatabase = Database.getInstance();
    }

    public void addVocToNote(int id, int index) {
        mDatabase.addVocToNote(id, index);
    }

    public void addNewNote(String newNote) {
        mDatabase.createNewNote(newNote);
    }

    public LinkedList<HashMap> createSearchResultMap(String searchStr) {

        ArrayList<Vocabulary> vocabularies = mDatabase.readSuggestVocabularyBySpell(searchStr);

        if (vocabularies == null)
            return null;

        LinkedList<HashMap> dataMap = new LinkedList<>();
        for(Vocabulary voc : vocabularies){
            HashMap<String, Object> hm = new HashMap<>();
            String from[] = SearchListView.LIST_ITEM_CONTENT_FROM;

            hm.put("vocId", voc.getId());
            hm.put(from[SearchListView.IDX_VOC_SPELL], voc.getSpell());
            hm.put(from[SearchListView.IDX_VOC_TRANSLATION], voc.getTranslation());
            hm.put(from[SearchListView.IDX_VOC_CATEGORY], voc.getPartOfSpeech());
            dataMap.add(hm);
        }
        return dataMap;
    }

    public HashMap<String, Object> createSearchResultDetailMap(Vocabulary voc) {
        HashMap<String, Object> hm = new HashMap<>();
        String from[] = SearchDetailView.DETAIL_ITEM_CONTENT_FROM;
        hm.put(from[SearchDetailView.IDX_VOC_SPELL], voc.getSpell());
        hm.put(from[SearchDetailView.IDX_VOC_KK], voc.getPhonetic());
        hm.put(from[SearchDetailView.IDX_VOC_TRANSLATION], voc.getTranslation());
        hm.put(from[SearchDetailView.IDX_VOC_SENTENCE], voc.getEnSentence());
        hm.put(from[SearchDetailView.IDX_VOC_SENTENCE_TRANSLATION], voc.getCnSentence());
        return hm;
    }

    public LinkedList<String> getNoteNameList() {
        ArrayList<String> noteNameArrayList = mDatabase.getNoteNames();
        return toLinkedList(noteNameArrayList);
    }

    private <T> LinkedList<T> toLinkedList(ArrayList<T> arrayList) {
        LinkedList<T> linkedList = new LinkedList<>();
        for (T t : arrayList) {
            linkedList.add(t);
        }
        return linkedList;
    }
}
