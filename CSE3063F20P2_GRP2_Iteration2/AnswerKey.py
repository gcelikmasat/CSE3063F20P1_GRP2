import logging
class AnswerKey:
    def __init__(self, poll_name, question,answer):
        self.poll_name = poll_name
        self.question = question
        self.question.answers = answer
        self.used=False
            # print created question into log file
    def trace(self):
        message = " Question created: " + self.question.text + " created"
        logging.info(message)
            # print poll name and question answers to console
    def toString(self):
        print("***************************** Answer key **************************")
        print("Poll name: ", self.poll_name)
        self.question.toString()
