package ru.netology

// Класс для хранения заметок
data class Note(
    val noteId: String,     // Идентификатор заметки.
    val title: String,    // Заголовок заметки.
    val text: String,    // Текст заметки.
    val privacy: Int,    // Уровень доступа к заметке. Возможные значения: (0, 1, 2, 3)
    val commentPrivacy: Int,   // Уровень доступа к комментированию заметки. Возможные значения: (0, 1, 2, 3)
    val privacyView: String,     // Настройки приватности просмотра заметки в специальном формате.
    val privacyComment: String  // Настройки приватности комментирования заметки в специальном формате.
)

// Класс для хранения комментария
data class Comment(
    val commentId: Int,   // Идентификатор комментария.
    val noteId: String,
    val message: String,  // Текст комментария.
    val isDeleted: Boolean = false // Флаг -> Комментарий удален или нет
)

// класс Заметки
// =====================
class Notes() {

    private val notes = mutableListOf<Note>()
    private val comments = mutableListOf<Comment>()
    private var commentIdCounter = 0

    // Исключение для обработки ошибок
    class MyCustomExceptionNote(message: String) : Exception(message)
    class MyCustomExceptionComment(message: String) : Exception(message)

    // Метод add
    // Создает новую заметку у текущего пользователя.
    fun add(note: Note) {
        notes.add(note)
    }

    // Метод createComment
    // Добавляет новый комментарий к заметке.
    fun createComment(noteId: String, message: String) {
        if (notes.none { it.noteId == noteId }) {
            throw MyCustomExceptionNote("Заметка с ID $noteId не найдена.")
        }
        val comment = Comment(commentIdCounter++, noteId, message)
        comments.add(comment)
    }

    // Метод delete
    // Удаляет заметку текущего пользователя.
    fun delete(noteId: String) {
        val note = notes.find { it.noteId == noteId } ?: throw MyCustomExceptionNote("Заметка с ID $noteId не найдена.")
        notes.remove(note)
    }

    // Метод deleteComment
    // Удаляет комментарий к заметке.
    fun deleteComment(commentId: Int) {
        val index = comments.indexOfFirst { it.commentId == commentId }
        if (index != -1) {
            val comment = comments[index]
            comments[index] = comment.copy(isDeleted = true) // Помечаем комментарий как удаленный
        } else {
            throw Exception("Комментарий с ID $commentId не найден.")
        }
    }

    // Метод edit
    // Редактирует заметку текущего пользователя.
    fun edit(noteId: String, newNote: Note) {
        val index = notes.indexOfFirst { it.noteId == noteId }
        if (index == -1) throw MyCustomExceptionNote("Заметка с ID $noteId не найдена.")
        notes[index] = newNote
    }

    // Метод editComment
    // Редактирует указанный комментарий у заметки.
    fun editComment(commentId: Int, newMessage: String) {
        val index = comments.indexOfFirst { it.commentId == commentId }
        if (index == -1) throw MyCustomExceptionComment("Комментарий с ID $commentId не найден.")
        comments[index] = comments[index].copy(message = newMessage)
    }

    // Метод get
    // Возвращает список заметок, созданных пользователем.
    fun get(): List<Note> {
        return notes.toList()
    }

    // Метод getById
    // Возвращает заметку по её id.
    fun getById(noteId: String): Note {
        return notes.find { it.noteId == noteId } ?: throw MyCustomExceptionNote("Заметка с ID $noteId не найдена.")
    }

    // Метод getComments
    // Возвращает список комментариев к заметке
    fun getComments(noteId: String): List<Comment> {
        return comments.filter { it.noteId == noteId && !it.isDeleted }  // исключаем удаленные комментарии
    }

    // Метод restoreComment
    // Восстанавливает удалённый комментарий по его ID.
    fun restoreComment(commentId: Int) {
        val index = comments.indexOfFirst { it.commentId == commentId && it.isDeleted }
        if (index != -1) {
            val comment = comments[index]
            comments[index] = comment.copy(isDeleted = false) // Восстанавливаем комментарий
        } else {
            throw Exception("Комментарий с ID $commentId не найден или не удален.")
        }
    }
}
// =====================

fun main() {
    // Проверка работоспособности кода
    // ++++++++++++++++++++++++++++++++++

    // Создаем экземпляр класса Notes
    val notesClass = Notes()

    // Создаем заметки
    val note1 = Note("1", "Заметка 1", "Текст заметки 1", 1, 1, "private", "private")
    val note2 = Note("2", "Заметка 2", "Текст заметки 2", 2, 2, "public", "public")

    // Добавляем заметки
    notesClass.add(note1)
    notesClass.add(note2)

    // Получаем и выводим все заметки
    println("Все заметки:")
    notesClass.get().forEach { println(it) }

    // Создаем комментарии к заметке 1
    notesClass.createComment("1", "Комментарий 1 к заметке 1")
    notesClass.createComment("1", "Комментарий 2 к заметке 1")

    // Получаем и выводим комментарии к заметке 1
    println("\nКомментарии к заметке 1:")
    notesClass.getComments("1").forEach { println(it) }

    // Редактируем заметку 1
    val editedNote1 = note1.copy(title = "Обновленная Заметка 1", text = "Обновленный текст заметки 1")
    notesClass.edit("1", editedNote1)

    // Получаем и выводим обновленную заметку 1
    println("\nОбновленная заметка 1:")
    println(notesClass.getById("1"))

    // Удаляем комментарий
    val comments = notesClass.getComments("1")
    if (comments.isNotEmpty()) {
        notesClass.deleteComment(comments[0].commentId) // Удаляем первый комментарий
    }
    println("\nКомментарии к заметке 1 после удаления первого комментария:")
    notesClass.getComments("1").forEach { println(it) }

    // Удаляем заметку 2
    notesClass.delete("2")
    println("\nВсе заметки после удаления заметки 2:")
    notesClass.get().forEach { println(it) }
    // ++++++++++++++++++++++++++++++++++
}