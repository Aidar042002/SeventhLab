package client.utility;

import client.Main;
import common.data.*;
import common.exceptions.IncorrectInputInScriptException;
import common.exceptions.MustBeNotEmptyException;
import common.exceptions.NotInDeclaredLimitsException;
import common.utility.Outputer;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class ProductAsker {
    private Scanner userScanner;
    private boolean fileMode;

    public ProductAsker(Scanner userScanner) {
        this.userScanner = userScanner;
        fileMode = false;
    }

    public void setFileMode() {
        fileMode = true;
    }

    public String askName() throws IncorrectInputInScriptException {
        String name;
        while (true) {
            try {
                Outputer.println("Введите имя:");
                Outputer.print(Main.PS2);
                name = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(name);
                if (name.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Имя не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (MustBeNotEmptyException exception) {
                Outputer.printerror("Имя не может быть пустым!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return name;
    }


    public double askX() throws IncorrectInputInScriptException {
        String strX;
        double x;
        while (true) {
            try {
                Outputer.println("Введите координату X:");
                Outputer.print(Main.PS2);
                strX = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strX);
                x = Double.parseDouble(strX);
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Координата X не распознана!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("Координата X должна быть представлена числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return x;
    }

    public Float askY() throws IncorrectInputInScriptException {
        String strY;
        Float y;
        while (true) {
            try {
                Outputer.println("Введите координату Y < " + (Product.MAX_Y + 1) + ":");
                Outputer.print(Main.PS2);
                strY = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strY);
                y = Float.parseFloat(strY);
                if (y > Product.MAX_Y) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Координата Y не распознана!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("Координата Y не может превышать " + Product.MAX_Y + "!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("Координата Y должна быть представлена числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return y;
    }

    public Coordinates askCoordinates() throws IncorrectInputInScriptException {
        double x;
        Float y;
        x = askX();
        y = askY();
        return new Coordinates(x, y);
    }


    public double askPrice() throws IncorrectInputInScriptException {
        String strHealth;
        double health;
        while (true) {
            try {
                Outputer.println("Введите стоимость:");
                Outputer.print(Main.PS2);
                strHealth = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strHealth);
                health = Double.parseDouble(strHealth);
                if (health <= Product.MIN_HEALTH) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Стоимость  не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("Стоимость должна быть больше нуля!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("Стоимость  должна быть представлено числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return health;
    }

    public UnitOfMeasure askUnitOfMeasure() throws IncorrectInputInScriptException {
        String strCategory;
        UnitOfMeasure category;
        while (true) {
            try {
                Outputer.println("Список единиц измерения - " + UnitOfMeasure.nameList());
                Outputer.println("Введите единицу измерения:");
                Outputer.print(Main.PS2);
                strCategory = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strCategory);
                category = UnitOfMeasure.valueOf(strCategory.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalArgumentException exception) {
                Outputer.printerror("Этого нет в списке!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return category;
    }

    public OrganizationType askOrgType() throws IncorrectInputInScriptException {
        String strWeaponType;
        OrganizationType organizationTypeType;
        while (true) {
            try {
                Outputer.println("Список типов организации- " + OrganizationType.nameList());
                Outputer.println("Введите тип:");
                Outputer.print(Main.PS2);
                strWeaponType = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strWeaponType);
                organizationTypeType = OrganizationType.valueOf(strWeaponType.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalArgumentException exception) {
                Outputer.printerror("Этого нет в списке!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return organizationTypeType;
    }


    public Location askLOcation() throws IncorrectInputInScriptException {
        String strMeleeWeapon;
        Location location;
        while (true) {
            try {
                Outputer.println("Список локаций - " + Location.nameList());
                Outputer.println("Введите локацию:");
                Outputer.print(Main.PS2);
                strMeleeWeapon = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strMeleeWeapon);
                location = Location.valueOf(strMeleeWeapon.toUpperCase());
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalArgumentException exception) {
                Outputer.printerror("Этого нет в списке!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return location;
    }

    public String askOrgName() throws IncorrectInputInScriptException {
        String chapterName;
        while (true) {
            try {
                Outputer.println("Введите имя организации:");
                Outputer.print(Main.PS2);
                chapterName = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(chapterName);
                if (chapterName.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Имя не распознано!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (MustBeNotEmptyException exception) {
                Outputer.printerror("Имя не может быть пустым!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return chapterName;
    }

    public long askChapterMarinesCount() throws IncorrectInputInScriptException {
        String strMarinesCount;
        long marinesCount;
        while (true) {
            try {
                Outputer.println("Введите zip < " + (Product.MAX_MARINES + 1) + ":");
                Outputer.print(Main.PS2);
                strMarinesCount = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(strMarinesCount);
                marinesCount = Long.parseLong(strMarinesCount);
                if (marinesCount < Product.MIN_MARINES || marinesCount > Product.MAX_MARINES)
                    throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Zip не распознан!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("Zip должен быть положительным и не превышать " + Product.MAX_MARINES + "!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NumberFormatException exception) {
                Outputer.printerror("Zip должен быть представлено числом!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NullPointerException | IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return marinesCount;
    }

    public Organization askOrg() throws IncorrectInputInScriptException {
        String name;
        long marinesCount;
        name = askOrgName();
        marinesCount = askChapterMarinesCount();
        return new Organization(name, marinesCount);
    }

    public boolean askQuestion(String question) throws IncorrectInputInScriptException {
        String finalQuestion = question + " (+/-):";
        String answer;
        while (true) {
            try {
                Outputer.println(finalQuestion);
                Outputer.print(Main.PS2);
                answer = userScanner.nextLine().trim();
                if (fileMode) Outputer.println(answer);
                if (!answer.equals("+") && !answer.equals("-")) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Ответ не распознан!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("Ответ должен быть представлен '+' или '-'!");
                if (fileMode) throw new IncorrectInputInScriptException();
            } catch (IllegalStateException exception) {
                Outputer.printerror("Непредвиденная ошибка!");
                System.exit(0);
            }
        }
        return answer.equals("+");
    }
}