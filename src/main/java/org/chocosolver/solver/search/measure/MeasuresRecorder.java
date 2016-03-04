/**
 * Copyright (c) 2015, Ecole des Mines de Nantes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    This product includes software developed by the <organization>.
 * 4. Neither the name of the <organization> nor the
 *    names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY <COPYRIGHT HOLDER> ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.chocosolver.solver.search.measure;


import org.chocosolver.solver.Model;
/**
 * Object which stores resolution information to get statistics
 *
 * @author Charles Prud'Homme
 */
public final class MeasuresRecorder implements IMeasures, Cloneable {

    /**
     * To transform time from nanoseconds to seconds
     */
    private static final float IN_SEC = 1000 * 1000 * 1000f;

    /**
     * Indicates if an objective is declared (<tt>false</tt> means satisfaction problem).
     */
    public boolean hasObjective;

    /**
     * Indicates if the optimal value has been proven for the objective (set to <tt>true</tt>).
     */
    public boolean objectiveOptimal;

    /**
     * Counts the number of solutions found so far.
     */
    public long solutionCount;

    /**
     * Counts the time spent so far, starting from solver construction call.
     */
    public long timeCount;

    /**
     * Counts the time spent into reading the model
     */
    public long readingTimeCount;

    /**
     * Counts the number of nodes opened so far.
     */
    public long nodeCount;

    /**
     * Counts the number of backtracks done so far.
     */
    public long backtrackCount;

    /**
     * Counts the number of failures encountered so far.
     */
    public long failCount;

    /**
     * Counts the number of restarts done so far.
     */
    public long restartCount;

    /**
     * Stores the overall maximum depth
     */
    public long maxDepth;

    /**
     * Stores the current depth
     */
    public long depth;

    /**
     * When the clock watch starts
     */
    protected long startingTime;

    /**
     * The solver to measure
     */
    protected Model model;

    /**
     * Create a measures recorder observing a <code>solver</code>
     * @param model the solver to observe
     */
    public MeasuresRecorder(Model model) {
        super();
        this.model = model;
    }


    /**
     * Clones the IMeasure object (copy every measure)
     *
     * @return a new instance of IMeasure
     */
    public MeasuresRecorder duplicate() {
        MeasuresRecorder mr = new MeasuresRecorder(model);
        mr.hasObjective = hasObjective;
        mr.objectiveOptimal = objectiveOptimal;
        mr.solutionCount = solutionCount;
        mr.timeCount = timeCount;
        mr.readingTimeCount = readingTimeCount;
        mr.nodeCount = nodeCount;
        mr.backtrackCount = backtrackCount;
        mr.failCount = failCount;
        mr.restartCount = restartCount;
        mr.startingTime = startingTime;
        mr.maxDepth = maxDepth;
        mr.depth = depth;
        return mr;
    }

    //****************************************************************************************************************//
    //**************************************** SETTERS ***************************************************************//
    //****************************************************************************************************************//

    @Override
    public long getSolutionCount() {
        return solutionCount;
    }

    /**
     * indicates an objective variable
     * @param ho set to <tt>true<tt/> to indicate that an objective is declared
     */
    public void declareObjective(boolean ho) {
        hasObjective = ho;
    }

    /**
     * indicates whether or not the optimum has been found and proved
     * @param objectiveOptimal <tt>true</tt> if the objective is proven to be optimal
     */
    public void setObjectiveOptimal(boolean objectiveOptimal) {
        this.objectiveOptimal = objectiveOptimal;
    }

    /**
     * Reset every measure to its default value (mostly 0)
     */
    public final void reset() {
        timeCount = 0;
        nodeCount = 0;
        backtrackCount = 0;
        restartCount = 0;
        failCount = 0;
        solutionCount = 0;
        depth = 0;
        maxDepth = 0;
    }

    //****************************************************************************************************************//
    //**************************************** GETTERS ***************************************************************//
    //****************************************************************************************************************//

    @Override
    public long getBackTrackCount() {
        return backtrackCount;
    }

    @Override
    public long getFailCount() {
        return failCount;
    }

    @Override
    public long getNodeCount() {
        return nodeCount;
    }

    @Override
    public float getTimeCount() {
        return timeCount / IN_SEC;
    }

    /**
     * Returns the elapsed time in nanoseconds
     * @return the elapsed time in nanoseconds
     */
    public long getElapsedTimeInNanoseconds() {
        return timeCount;
    }

    @Override
    public float getReadingTimeCount() {
        return readingTimeCount / IN_SEC;
    }

    /**
     * set the reading time count
     * @param time time needed to read the model
     */
    public void setReadingTimeCount(long time) {
        this.readingTimeCount = time;
    }

    @Override
    public long getRestartCount() {
        return restartCount;
    }

    @Override
    public long getMaxDepth() {
        return maxDepth;
    }

    @Override
    public long getCurrentDepth() {
        return depth;
    }

    @Override
    public boolean isObjectiveOptimal() {
        return objectiveOptimal;
    }

    @Override
    public boolean hasObjective() {
        return hasObjective;
    }

    @Override
    public Number getBestSolutionValue() {
        return model.getSolver().getObjectiveManager().getBestSolutionValue();
    }

    @Override
    public long getTimestamp() {
        return nodeCount + backtrackCount;
    }

    //****************************************************************************************************************//
    //**************************************** INCREMENTERS **********************************************************//
    //****************************************************************************************************************//

    /**
     * increment node counter
     */
    public void incNodeCount() {
        nodeCount++;
        if (depth > maxDepth) {
            maxDepth = depth;
        }
    }

    /**
     * increment backtrack counter
     */
    public void incBackTrackCount() {
        backtrackCount++;
    }

    /**
     * increment fail counter
     */
    public void incFailCount() {
        failCount++;
    }

    /**
     * increment restart counter
     */
    public void incRestartCount() {
        restartCount++;
    }

    /**
     * increment solution counter
     */
    public void incSolutionCount() {
        solutionCount++;
        updateTime();
    }

    /**
     * Increments current depth
     */
    public void incDepth(){
        depth++;
    }

    /**
     * Decrements current depth
     */
    public void decDepth(){
        depth--;
    }

    /**
     * Start the stopwatch, to compute resolution time
     */
    public void startStopwatch() {
        startingTime = System.nanoTime();
    }

    /**
     * Update resolution time
     */
    public void updateTime() {
        timeCount = System.nanoTime() - startingTime;
    }

    //****************************************************************************************************************//
    //**************************************** PRINTERS **************************************************************//
    //****************************************************************************************************************//

    /**
     * @return statistic values only
     */
    public Number[] toArray() {
        return new Number[]{
                getSolutionCount(),
                getReadingTimeCount(),
                getTimeCount(),
                hasObjective() ? getBestSolutionValue() : 0,
                getNodeCount(),
                getBackTrackCount(),
                getFailCount(),
                getRestartCount()
        };
    }

    /**
     * @return a summary of recorded statistics
     */
    public String toOneLineString() {
        updateTime();
        StringBuilder st = new StringBuilder(256);
        st.append("Model[").append(model.getName()).append("], ");
        st.append(String.format("%d Solutions, ", solutionCount));
        if (hasObjective()) {
            st.append(model.getSolver().getObjectiveManager()).append(", ");
        }
        st.append(String.format("Resolution time %.3fs, %d Nodes (%,.1f n/s), %d Backtracks, %d Fails, %d Restarts",
                getTimeCount(),
                getNodeCount(),
                getNodeCount() / getTimeCount(),
                getBackTrackCount(),
                getFailCount(),
                getRestartCount()));
        return st.toString();
    }

    /**
     * @return statistics in a CSV format
     */
    public String toCSV() {
        updateTime();
        // solutionCount;buildingTime(sec);initTime(sec);initPropag(sec);totalTime(sec);objective;nodes;backtracks;fails;restarts;fineProp;coarseProp;
        return String.format("%d;%.3f;%.3f;%e;%d;%d;%d;%d;",
                getSolutionCount(),
                getReadingTimeCount(),
                getTimeCount(),
                hasObjective() ? getBestSolutionValue().doubleValue() : 0,
                getNodeCount(),
                getBackTrackCount(),
                getFailCount(),
                getRestartCount());
    }

    @Override
    public String toString() {
        updateTime();
        StringBuilder st = new StringBuilder(256);
//        st.append("- Search statistics\n");
        if (model.getSolver().isStopCriterionMet()) {
            st.append("- Incomplete search - Limit reached.\n");
        } else if (model.getSolver().hasEndedUnexpectedly()) {
            st.append("- Incomplete search - Unexpected interruption.\n");
        } else {
            st.append("- Complete search - ");
            if (solutionCount == 0) {
                st.append("No solution.");
            } else if (solutionCount == 1) {
                st.append("1 solution found.");
            } else {
                st.append(String.format("%,d solution(s) found.", solutionCount));
            }
            st.append('\n');
        }
        st.append("\tModel[").append(model.getName()).append("]\n");
        st.append(String.format("\tSolutions: %,d\n", solutionCount));
        if (hasObjective()) {
            st.append("\t").append(model.getSolver().getObjectiveManager()).append(",\n");
        }
        st.append(String.format("\tBuilding time : %,.3fs" +
                        "\n\tResolution time : %,.3fs\n\tNodes: %,d (%,.1f n/s) \n\tBacktracks: %,d\n\tFails: %,d\n\t" +
                        "Restarts: %,d\n\tVariables: %,d\n\tConstraints: %,d",
                getReadingTimeCount(),
                getTimeCount(),
                getNodeCount(),
                getNodeCount() / getTimeCount(),
                getBackTrackCount(),
                getFailCount(),
                getRestartCount(),
                model.getNbVars(),
                model.getNbCstrs()
        ));
        return st.toString();
    }
}