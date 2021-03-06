package io.holunda.camunda.taskpool.example.process.process

import io.holunda.camunda.taskpool.example.process.process.ProcessApproveRequest.Variables.ON_BEHALF
import io.holunda.camunda.taskpool.example.process.process.ProcessApproveRequest.Variables.ORIGINATOR
import io.holunda.camunda.taskpool.example.process.process.ProcessApproveRequest.Variables.SUBJECT
import io.holunda.camunda.taskpool.example.process.process.ProcessApproveRequest.Variables.TARGET
import org.camunda.bpm.engine.RuntimeService
import org.camunda.bpm.engine.TaskService
import org.camunda.bpm.engine.runtime.ProcessInstance
import org.camunda.bpm.engine.variable.Variables
import org.camunda.bpm.engine.variable.Variables.stringValue
import org.h2.expression.Variable
import org.springframework.stereotype.Component
import java.util.*

object ProcessApproveRequest {
  const val KEY = "process_approve_request"
  const val RESOURCE = "process_approve_request.bpmn"

  object Variables {
    const val ORIGINATOR = "originator"
    const val ON_BEHALF = "on-behalf-of"
    const val SUBJECT = "subject"
    const val TARGET = "target"
    const val APPROVE_DECISION = "approveDecision"
    const val AMEND_ACTION = "amendAction"
  }

  object Elements {
    const val APPROVE_REQUEST = "user_approve_request"
    const val AMEND_REQUEST = "user_amend_request"
  }
}

@Component
class ProcessApproveRequestBean(
  private val runtimeService: RuntimeService,
  private val taskService: TaskService
) {

  fun startProcess(): ProcessInstance {
    return runtimeService.startProcessInstanceByKey(ProcessApproveRequest.KEY,
      "AR-${UUID.randomUUID()}",
      Variables.createVariables()
        .putValue(ORIGINATOR, "kermit")
        .putValue(ON_BEHALF, "piggy")
        .putValue(SUBJECT, "Salary increase")
        .putValue(TARGET, "1,000,000.00 USD/Y")
    )
  }

  fun approve(id: String, decision: String) {

    if (!arrayOf("APPROVE", "RETURN", "REJECT").contains(decision.toUpperCase())) {
      throw IllegalArgumentException("Only one of APPROVE, RETURN, REJECT is supported.")
    }

    val task = taskService.createTaskQuery().processInstanceId(id).taskDefinitionKey(ProcessApproveRequest.Elements.APPROVE_REQUEST).singleResult()
    taskService.claim(task.id, "gonzo")
    taskService.complete(task.id, Variables.createVariables().putValue(ProcessApproveRequest.Variables.APPROVE_DECISION, stringValue(decision.toUpperCase())))
  }

  fun amend(id: String, action: String) {

    if (!arrayOf("CANCEL", "RESUBMIT").contains(action.toUpperCase())) {
      throw IllegalArgumentException("Only one of CANCEL, RESUBMIT is supported.")
    }


    val task = taskService.createTaskQuery().processInstanceId(id).taskDefinitionKey(ProcessApproveRequest.Elements.AMEND_REQUEST).singleResult()
    taskService.complete(task.id, Variables.createVariables().putValue(ProcessApproveRequest.Variables.AMEND_ACTION, stringValue(action.toUpperCase())))
  }



  fun countInstances() = getAllInstancesQuery().count()

  fun deleteAllInstances() {
    getAllInstancesQuery().list().forEach { runtimeService.deleteProcessInstance(it.processInstanceId, "Deleted by the mass deletion REST call") }
  }

  private fun getAllInstancesQuery() = runtimeService.createProcessInstanceQuery().processDefinitionKey(ProcessApproveRequest.KEY)
}

