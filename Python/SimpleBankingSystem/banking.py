# Write your code here
import credit_card
import DB_Storage


class BankingService:
    def __init__(self):
        self.working_session = True
        self.main_menu = {1: ("Create an account", self.create_account), 2: ("Log into account", self.log_into_account), 0: ("Exit", self.exit) }
        self.account_menu = {1: ("Balance", self.get_balance), 2: ("Log out", self.log_out), 0: ("Exit", self.exit)}
        self.current_menu = self.main_menu
        self.current_account = None
        self.db_connect = DB_Storage.db_create_connection()
        if self.db_connect is not None:
            self.card_generator = credit_card.CreditCardManager(self.db_connect)

    def __check_account(self, card_num, card_pin):
        res = DB_Storage.db_check_account(self.db_connect, card_num, card_pin)
        if res is not None:
            return True, credit_card.CreditCard(*res)
        return False, None
        #for account in self.registered_accounts:
        #    if card_num == account.card_num and card_pin == account.pin_code:
        #        return True, account
        #return False, None

    def create_account(self):
        new_credit_card = credit_card.create_credit_card(self.card_generator)
        DB_Storage.db_add_account(self.db_connect, new_credit_card.card_num, new_credit_card.pin_code)
        print("Your card has been created", "Your card number:", new_credit_card.card_num, "Your card PIN:", new_credit_card.pin_code, sep='\n')

    def log_into_account(self):
        print("Enter your card number")
        card_num_inp = input()
        print("Enter your PIN:")
        card_pin_inp = input()
        res, acc = self.__check_account(card_num_inp, card_pin_inp)
        if res:
            print("You have successfully logged in!")
            self.current_menu = self.account_menu
            self.current_account = acc
        else:
            print("Wrong card number or PIN!")

    def log_out(self):
        print("You have successfully logged out")
        self.current_account = None
        self.current_menu = self.main_menu

    def get_balance(self):
        print("Balance:", self.current_account.balance)

    def exit(self):
        self.working_session = False

    def menu_output(self):
        for item, name in self.current_menu.items():
            print(str(item) + ".", name[0])

    def handle_menu(self):
        while self.working_session:

            if self.db_connect is None:
                self.exit()

            self.menu_output()
            try:
                usr_inp = int(input())
                self.current_menu[usr_inp][1]()
            except (KeyError, ValueError):
                print("Something goes wrong try again")
        print("Bye!")


if __name__ == "__main__":
    bankService = BankingService()
    bankService.handle_menu()

