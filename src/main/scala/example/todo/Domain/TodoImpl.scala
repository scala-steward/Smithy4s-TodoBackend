package example.todo.Domain

import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import example.todo._
import example.todo.Storage.TodoRepo

class TodoImpl(todoRepo: TodoRepo[IO]) extends TodoService[IO] {
  override def createTodo(
      title: Title,
      description: Option[TodoDescription]
  ): IO[CreateTodoOutput] =
    todoRepo.createTodo(title, description).map(CreateTodoOutput(_))

  override def getTodo(id: Id): IO[GetTodoOutput] =
    todoRepo.getTodo(id).flatMap {
      case Some(todo) => IO.pure(GetTodoOutput(todo))
      case None       => IO.raiseError(TodoNotFound("Todo not found"))
    }

  override def updateTodo(
      id: Id,
      name: Option[Title],
      description: Option[TodoDescription],
      status: Option[TodoStatus]
  ): IO[Unit] =
    todoRepo.updateTodo(id, name, description, status)

  override def deleteTodo(id: Id): IO[Unit] =
    todoRepo.deleteTodo(id)

  override def listTodos(): IO[ListTodosOutput] =
    todoRepo.listTodos().map(ListTodosOutput(_))
}

object TodoImpl {
  def apply(todoRepo: TodoRepo[IO]): IO[TodoService[IO]] = {
    new TodoImpl(todoRepo).pure[IO]
  }
}
