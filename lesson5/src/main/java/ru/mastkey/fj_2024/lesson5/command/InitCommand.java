package ru.mastkey.fj_2024.lesson5.command;

import java.util.concurrent.Future;

public interface InitCommand {
    Future<Void> execute();
}
