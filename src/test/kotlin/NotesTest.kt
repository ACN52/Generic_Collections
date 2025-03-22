package ru.netology

import org.junit.Assert.*
import org.junit.Test

class NotesTest {

    private val notesClass = Notes()

    // Метод add
    // Создает новую заметку у текущего пользователя.
    @Test
    fun testAddNote() {
        val note = Note("1", "Заметка 1", "Текст заметки 1", 1, 1, "private", "private")
        notesClass.add(note)
        assertEquals(1, notesClass.get().size)
        assertEquals(note, notesClass.getById("1"))
    }

    // Метод createComment
    // Добавляет новый комментарий к заметке.
    @Test
    fun testCreateComment() {
        val note = Note("1", "Заметка 1", "Текст заметки 1", 1, 1, "private", "private")
        notesClass.add(note)
        notesClass.createComment("1", "Комментарий 1")
        assertEquals(1, notesClass.getComments("1").size)
    }

    // Exception - добавляем комментарий к несуществующей заметке
    @Test(expected = Notes.MyCustomExceptionNote::class)
    fun testCreateComment_Exception() {
        notesClass.createComment("999", "Комментарий 1")
    }

    // Метод delete
    // Удаляет заметку текущего пользователя.
    @Test
    fun testDeleteNote() {
        val note = Note("1", "Заметка 1", "Текст заметки 1", 1, 1, "private", "private")
        notesClass.add(note)
        notesClass.delete("1")
        assertEquals(0, notesClass.get().size)
    }

    // Exception - удаляем несуществующую заметку
    @Test(expected = Notes.MyCustomExceptionNote::class)
    fun testDeleteNote_Exception() {
        notesClass.delete("999")
    }

    // Метод deleteComment
    // Удаляет комментарий к заметке.
    @Test
    fun testDeleteComment() {
        // Создаем заметку
        val note = Note("1", "Заметка 1", "Текст заметки 1", 1, 1, "private", "private")
        notesClass.add(note)

        // Добавляем комментарий
        notesClass.createComment("1", "Комментарий 1")
        val comments = notesClass.getComments("1")

        // Удаляем комментарий
        notesClass.deleteComment(comments[0].commentId)

        // Проверяем, что комментарий помечен как удаленный
        val updatedComments = notesClass.getComments("1") // Получаем обновленный список комментариев
        assertEquals(0, updatedComments.size) // Удаленные комментарии не должны отображаться в выборке

        // Проверяем, что комментарий помечен как удаленный в исходном списке
        val deletedComment = comments[0]
        assertFalse(deletedComment.isDeleted)
    }

    // Exception - удаляем несуществующий комментарий
    @Test(expected = Exception::class)
    fun testDeleteComment_Exception() {
        notesClass.deleteComment(999)
    }

    // Метод edit
    // Редактирует заметку текущего пользователя.
    @Test
    fun testEditNote() {
        val note = Note("1", "Заметка 1", "Текст заметки 1", 1, 1, "private", "private")
        notesClass.add(note)
        val editedNote = note.copy(title = "Обновленная Заметка 1")
        notesClass.edit("1", editedNote)
        assertEquals(editedNote, notesClass.getById("1"))
    }

    // Exception - редактируем несуществующую заметку
    @Test(expected = Notes.MyCustomExceptionNote::class)
    fun testEditNote_Exception() {
        val note = Note("1", "Заметка 1", "Текст заметки 1", 1, 1, "private", "private")
        notesClass.edit("999", note)
    }

    // Метод editComment
    // Редактирует указанный комментарий у заметки.
    @Test
    fun testEditComment() {
        val note = Note("1", "Заметка 1", "Текст заметки 1", 1, 1, "private", "private")
        notesClass.add(note)
        notesClass.createComment("1", "Комментарий 1")
        val comments = notesClass.getComments("1")
        notesClass.editComment(comments[0].commentId, "Обновленный комментарий")
        assertEquals("Обновленный комментарий", notesClass.getComments("1")[0].message)
    }

    // Exception - редактируем несуществующий комментарий
    @Test(expected = Notes.MyCustomExceptionComment::class)
    fun testEditComment_Exception() {
        notesClass.editComment(999, "Обновленный комментарий")
    }

    // Метод get
    // Возвращает список заметок, созданных пользователем.
    @Test
    fun testGetNote() {
        val note1 = Note("1", "Заметка 1", "Текст заметки 1", 1, 1, "private", "private")
        val note2 = Note("2", "Заметка 2", "Текст заметки 2", 1, 1, "private", "private")
        notesClass.add(note1)
        notesClass.add(note2)
        assertEquals(2, notesClass.get().size)
    }

    // Метод getComments
    // Возвращает список комментариев к заметке.
    @Test
    fun testGetComments() {
        val note = Note("1", "Заметка 1", "Текст заметки 1", 1, 1, "private", "private")
        notesClass.add(note)
        notesClass.createComment("1", "Комментарий 1")
        notesClass.createComment("1", "Комментарий 2")
        assertEquals(2, notesClass.getComments("1").size)
    }

    // Метод restoreComment
    // Восстанавливает удалённый комментарий.
    @Test
    fun testRestoreComment() {
        val note = Note("1", "Заметка 1", "Текст заметки 1", 1, 1, "private", "private")
        notesClass.add(note)
        notesClass.createComment("1", "Комментарий 1")
        val comments = notesClass.getComments("1")
        val deletedComment = comments[0]
        notesClass.deleteComment(deletedComment.commentId)
        notesClass.restoreComment(deletedComment.commentId) // Восстанавливаем по ID
        assertEquals(1, notesClass.getComments("1").size)
        assertFalse(notesClass.getComments("1")[0].isDeleted) // Проверяем, что комментарий восстановлен
    }
}