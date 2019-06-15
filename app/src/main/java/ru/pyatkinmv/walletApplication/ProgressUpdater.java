package ru.pyatkinmv.walletApplication;

import org.bitcoinj.core.listeners.DownloadProgressTracker;

import java.util.Date;

public abstract class ProgressUpdater extends DownloadProgressTracker {

    public abstract void done();

    public abstract void update(double pct, int blocksSoFar, Date date);
}
