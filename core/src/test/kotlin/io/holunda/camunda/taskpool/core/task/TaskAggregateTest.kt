package io.holunda.camunda.taskpool.core.task

import io.holunda.camunda.taskpool.api.business.addCorrelation
import io.holunda.camunda.taskpool.api.business.newCorrelations
import io.holunda.camunda.taskpool.api.task.*
import org.axonframework.test.aggregate.AggregateTestFixture
import org.camunda.bpm.engine.variable.Variables
import org.camunda.bpm.engine.variable.Variables.stringValue
import org.junit.Before
import org.junit.Test
import java.util.*

class TaskAggregateTest {

  private val fixture: AggregateTestFixture<TaskAggregate> = AggregateTestFixture<TaskAggregate>(TaskAggregate::class.java)
  private lateinit var now: Date

  @Before
  fun setUp() {
    now = Date()
  }

  @Test
  fun `should create task`() {
    fixture
      .givenNoPriorActivity()
      .`when`(
        CreateTaskCommand(
          id = "4711",
          name = "Foo",
          createTime = now,
          eventName = "create",
          owner = "kermit",
          taskDefinitionKey = "foo",
          formKey = "some",
          businessKey = "business123",
          enriched = true,
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          ),
          candidateUsers = listOf("kermit", "gonzo"),
          candidateGroups = listOf("muppets"),
          assignee = "kermit",
          priority = 51,
          description = "Funky task",
          payload = Variables.createVariables().putValueTyped("key", stringValue("value")),
          correlations = newCorrelations().addCorrelation("Request", "business123")
        ))
      .expectEvents(
        TaskCreatedEvent(
          id = "4711",
          name = "Foo",
          createTime = now,
          owner = "kermit",
          taskDefinitionKey = "foo",
          formKey = "some",
          businessKey = "business123",
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          ),
          candidateUsers = listOf("kermit", "gonzo"),
          candidateGroups = listOf("muppets"),
          assignee = "kermit",
          priority = 51,
          description = "Funky task",
          payload = Variables.createVariables().putValueTyped("key", stringValue("value")),
          correlations = newCorrelations().addCorrelation("Request", "business123")
        ))
  }

  @Test
  fun `should complete task`() {
    fixture
      .given(
        TaskCreatedEvent(
          id = "4711",
          name = "Foo",
          createTime = now,
          owner = "kermit",
          taskDefinitionKey = "foo",
          formKey = "some",
          businessKey = "business123",
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          ),
          candidateUsers = listOf("kermit", "gonzo"),
          candidateGroups = listOf("muppets"),
          assignee = "kermit",
          priority = 51,
          description = "Funky task",
          payload = Variables.createVariables().putValueTyped("key", stringValue("value")),
          correlations = newCorrelations().addCorrelation("Request", "business123")
        ))
      .`when`(
        CompleteTaskCommand(
          id = "4711",
          name = "Foo",
          createTime = now,
          eventName = "create",
          owner = "kermit",
          taskDefinitionKey = "foo",
          formKey = "some",
          businessKey = "business123",
          enriched = true,
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          ),
          dueDate = null,
          candidateUsers = listOf("kermit", "gonzo"),
          candidateGroups = listOf("muppets"),
          assignee = "kermit",
          priority = 51,
          description = "Funky task",
          payload = Variables.createVariables().putValueTyped("another", stringValue("some")),
          correlations = newCorrelations().addCorrelation("Request", "business456")
        ))
      .expectEvents(
        TaskCompletedEvent(
          id = "4711",
          name = "Foo",
          createTime = now,
          owner = "kermit",
          taskDefinitionKey = "foo",
          formKey = "some",
          businessKey = "business123",
          enriched = true,
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          ),
          dueDate = null,
          candidateUsers = listOf("kermit", "gonzo"),
          candidateGroups = listOf("muppets"),
          assignee = "kermit",
          priority = 51,
          description = "Funky task",
          payload = Variables.createVariables().putValueTyped("another", stringValue("some")),
          correlations = newCorrelations().addCorrelation("Request", "business456")
        )
      )
  }

  @Test
  fun `should assign task`() {
    fixture
      .given(
        TaskCreatedEvent(
          id = "4711",
          name = "Foo",
          taskDefinitionKey = "foo",
          formKey = "some",
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          )
        ))
      .`when`(
        AssignTaskCommand(
          id = "4711",
          name = "Foo",
          createTime = now,
          eventName = "create",
          owner = "kermit",
          taskDefinitionKey = "foo",
          formKey = "some",
          businessKey = "business123",
          enriched = true,
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          ),
          dueDate = null,
          candidateUsers = listOf("kermit", "gonzo"),
          candidateGroups = listOf("muppets"),
          assignee = "kermit",
          priority = 51,
          description = "Funky task",
          payload = Variables.createVariables().putValueTyped("another", stringValue("some")),
          correlations = newCorrelations().addCorrelation("Request", "business456")
        ))
      .expectEvents(
        TaskAssignedEvent(
          id = "4711",
          name = "Foo",
          createTime = now,
          owner = "kermit",
          taskDefinitionKey = "foo",
          formKey = "some",
          businessKey = "business123",
          enriched = true,
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          ),
          dueDate = null,
          candidateUsers = listOf("kermit", "gonzo"),
          candidateGroups = listOf("muppets"),
          assignee = "kermit",
          priority = 51,
          description = "Funky task",
          payload = Variables.createVariables().putValueTyped("another", stringValue("some")),
          correlations = newCorrelations().addCorrelation("Request", "business456")
        )
      )
  }


  @Test
  fun `should not re-assign task`() {
    fixture
      .given(
        TaskCreatedEvent(
          id = "4711",
          name = "Foo",
          taskDefinitionKey = "foo",
          formKey = "some",
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          )
        ),
        TaskAssignedEvent(
          id = "4711",
          name = "Foo",
          taskDefinitionKey = "foo",
          formKey = "some",
          assignee = "kermit",
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          )
        )
      )
      .`when`(
        AssignTaskCommand(
          id = "4711",
          name = "Foo",
          createTime = now,
          taskDefinitionKey = "foo",
          formKey = "some",
          assignee = "kermit",
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          )
        ))
      .expectNoEvents()
  }


  @Test
  fun `should not complete deleted task`() {
    fixture
      .given(
        TaskCreatedEvent(
          id = "4711",
          name = "Foo",
          createTime = now,
          owner = "kermit",
          taskDefinitionKey = "foo",
          formKey = "some",
          businessKey = "business123",
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          ),
          candidateUsers = listOf("kermit", "gonzo"),
          candidateGroups = listOf("muppets"),
          assignee = "kermit",
          priority = 51,
          description = "Funky task",
          payload = Variables.createVariables().putValueTyped("key", stringValue("value")),
          correlations = newCorrelations().addCorrelation("Request", "business123")
        ),
        TaskDeletedEvent(
          id = "4711",
          name = "Foo",
          createTime = now,
          owner = "kermit",
          taskDefinitionKey = "foo",
          formKey = "some",
          businessKey = "business123",
          enriched = true,
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          ),
          dueDate = null,
          candidateUsers = listOf("kermit", "gonzo"),
          candidateGroups = listOf("muppets"),
          assignee = "kermit",
          priority = 51,
          description = "Funky task",
          payload = Variables.createVariables().putValueTyped("another", stringValue("some1")),
          correlations = newCorrelations().addCorrelation("Request", "business789"),
          deleteReason = "Deleted, because not needed"
        ))
      .`when`(
        CompleteTaskCommand(
          id = "4711",
          name = "Foo",
          createTime = now,
          eventName = "create",
          owner = "kermit",
          taskDefinitionKey = "foo",
          formKey = "some",
          businessKey = "business123",
          enriched = true,
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          ),
          dueDate = null,
          candidateUsers = listOf("kermit", "gonzo"),
          candidateGroups = listOf("muppets"),
          assignee = "kermit",
          priority = 51,
          description = "Funky task",
          payload = Variables.createVariables().putValueTyped("another", stringValue("some")),
          correlations = newCorrelations().addCorrelation("Request", "business456")
        )
      ).expectNoEvents()
  }

  @Test
  fun `should not delete deleted task`() {
    fixture
      .given(
        TaskCreatedEvent(
          id = "4711",
          name = "Foo",
          createTime = now,
          owner = "kermit",
          taskDefinitionKey = "foo",
          formKey = "some",
          businessKey = "business123",
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          ),
          candidateUsers = listOf("kermit", "gonzo"),
          candidateGroups = listOf("muppets"),
          assignee = "kermit",
          priority = 51,
          description = "Funky task",
          payload = Variables.createVariables().putValueTyped("key", stringValue("value")),
          correlations = newCorrelations().addCorrelation("Request", "business123")
        ),
        TaskDeletedEvent(
          id = "4711",
          name = "Foo",
          createTime = now,
          owner = "kermit",
          taskDefinitionKey = "foo",
          formKey = "some",
          businessKey = "business123",
          enriched = true,
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          ),
          dueDate = null,
          candidateUsers = listOf("kermit", "gonzo"),
          candidateGroups = listOf("muppets"),
          assignee = "kermit",
          priority = 51,
          description = "Funky task",
          payload = Variables.createVariables().putValueTyped("another", stringValue("some1")),
          correlations = newCorrelations().addCorrelation("Request", "business789"),
          deleteReason = "Deleted, because not needed"
        ))
      .`when`(
        DeleteTaskCommand(
          id = "4711",
          taskDefinitionKey = "foo",
          deleteReason = "Not possible",
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          )
        )
      ).expectNoEvents()
  }

  @Test
  fun `should delete task`() {
    fixture
      .given(
        TaskCreatedEvent(
          id = "4711",
          name = "Foo",
          createTime = now,
          owner = "kermit",
          taskDefinitionKey = "foo",
          formKey = "some",
          businessKey = "business123",
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          ),
          candidateUsers = listOf("kermit", "gonzo"),
          candidateGroups = listOf("muppets"),
          assignee = "kermit",
          priority = 51,
          description = "Funky task",
          payload = Variables.createVariables().putValueTyped("key", stringValue("value")),
          correlations = newCorrelations().addCorrelation("Request", "business123")
        ))
      .`when`(
        DeleteTaskCommand(
          id = "4711",
          name = "Foo",
          createTime = now,
          eventName = "create",
          owner = "kermit",
          taskDefinitionKey = "foo",
          formKey = "some",
          businessKey = "business123",
          enriched = true,
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          ),
          dueDate = null,
          candidateUsers = listOf("kermit", "gonzo"),
          candidateGroups = listOf("muppets"),
          assignee = "kermit",
          priority = 51,
          description = "Funky task",
          payload = Variables.createVariables().putValueTyped("another", stringValue("some1")),
          correlations = newCorrelations().addCorrelation("Request", "business789"),
          deleteReason = "Deleted, because not needed"
        ))
      .expectEvents(
        TaskDeletedEvent(
          id = "4711",
          name = "Foo",
          createTime = now,
          owner = "kermit",
          taskDefinitionKey = "foo",
          formKey = "some",
          businessKey = "business123",
          enriched = true,
          sourceReference = ProcessReference(
            definitionKey = "process_key",
            instanceId = "0815",
            executionId = "12345",
            definitionId = "76543"
          ),
          dueDate = null,
          candidateUsers = listOf("kermit", "gonzo"),
          candidateGroups = listOf("muppets"),
          assignee = "kermit",
          priority = 51,
          description = "Funky task",
          payload = Variables.createVariables().putValueTyped("another", stringValue("some1")),
          correlations = newCorrelations().addCorrelation("Request", "business789"),
          deleteReason = "Deleted, because not needed"
        )
      )
  }
}
