package es.unizar.eina.notepad.ui.parcelas;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import es.unizar.eina.notepad.database.parcelas.Note;
import es.unizar.eina.notepad.database.parcelas.ParcelaRepository;

public class NoteViewModel extends AndroidViewModel {

    private ParcelaRepository mRepository;

    private final LiveData<List<Note>> mAllNotes;

    public NoteViewModel(Application application) {
        super(application);
        mRepository = new ParcelaRepository(application);
        mAllNotes = mRepository.getAllNotes();
    }

    LiveData<List<Note>> getAllNotes() { return mAllNotes; }

    public void insert(Note note) { mRepository.insert(note); }

    public void update(Note note) { mRepository.update(note); }
    public void delete(Note note) { mRepository.delete(note); }
}
