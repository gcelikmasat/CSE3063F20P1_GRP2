import logging


class Student:
    def __init__(self, id, FirstName, LastName, description):
        self.id = id
        self.FirstName = FirstName
        self.LastName = LastName
        self.description = description
        self.polls = []
        self.attendedClasses = 0
    #Adding polls to the array
    def addPoll(self, poll):
        self.polls.append(poll)
    #Printing polls
    def toString(self):
        print("--------------------------------------------------")
        print("Student ID: ", self.id, "\n",
              "Student First Name: ", self.FirstName, "\n",
              "Student Last Name: ", self.LastName, "\n",
              "Student Description: ", self.description, "\n")
        print("Polls: ")
        if len(self.polls) != 0:
            for poll in self.polls:
                poll.toString()
        #If poll does not exist, informint to the user about this
        else:
            print("no polls exist!")
    #This method is created for information on whether students are matched or not
    def checkStudent(self, fullname):
        #Defining Turkish characters to the system
        Tr2Eng = str.maketrans("çğıöşüÇĞIÖŞÜİi", "cgiosuCGIOSUII")
        fullname_bys = (self.FirstName + ' ' + self.LastName).translate(Tr2Eng).upper()
        names = fullname.split(" ")
        length = len(names)
        lastname = names[length - 1].translate(Tr2Eng).upper()
        firstnames = names[0: length - 1]
        exists = False
        #Checking students for matching by looking first names and last names
        if fullname_bys.find(lastname) != -1:

            for firstname in firstnames:
                firstname2 = firstname.translate(Tr2Eng).upper()
                #If the student is matched, exist variable returns as true
                if fullname_bys.find(firstname2) != -1:
                    exists = True
                # If the student is not matched, exist variable returns as false
                if len(firstnames) == 2 and lastname != self.LastName.translate(Tr2Eng).upper():
                    exists = False
        return exists
    #Poll checking
    def isPollInside(self, poll):
        for pollmy in self.polls:
            if poll.pollName in pollmy.pollName:
                return pollmy
    #Logging for creation of student
    def trace(self):
        message = " Student: " + self.FirstName + " " + self.LastName + " created"
        logging.info(message)
    #By looking attendance of polls, increasing attendance
    def increaseAttendance(self):
        self.attendedClasses += 1
    #Getting attendance
    def getAttendance(self):
        return self.attendedClasses
    #Set answer's situation, by looking it is correct or not
    def setCorrectAnswer(self):
        length = len(self.polls)
        if len(self.polls) != 0:
            #Looking for matching of poll's questions
            for count in range(length):
                for question in self.polls[count].questions:
                    # Looking for matching of poll's answer key and question key
                    for question_key in self.polls[count].answerKeys:
                        if question.text == question_key.question.text:
                            ##Looking for matching of answers
                            if question.answers in str(question_key.question.answers):
                                question.correct = True




