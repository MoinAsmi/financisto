/*******************************************************************************
 * Copyright (c) 2010 Denis Solonenko.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Denis Solonenko - initial API and implementation
 ******************************************************************************/
package ru.orangesoftware.financisto.report;

import android.content.Context;
import android.database.Cursor;
import ru.orangesoftware.financisto.filter.WhereFilter;
import ru.orangesoftware.financisto.filter.Criteria;
import ru.orangesoftware.financisto.db.DatabaseAdapter;
import ru.orangesoftware.financisto.db.DatabaseHelper.ReportColumns;
import ru.orangesoftware.financisto.graph.GraphUnit;
import ru.orangesoftware.financisto.model.Currency;
import ru.orangesoftware.financisto.db.DatabaseHelper;
import ru.orangesoftware.financisto.db.MyEntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;

public class PurificationReport extends Report {
    
    private static final String PURIFICATION_ATTRIBUTE = "Purification";

    public PurificationReport(Context context, Currency currency) {
        super(ReportType.PURIFICATION, context, currency);
    }

    @Override
    public ReportData getReport(DatabaseAdapter db, WhereFilter filter) {
        cleanupFilter(filter);
        ArrayList<GraphUnit> units = new ArrayList<>();
        Cursor cursor = null;
        try {
            // First get all Purification attribute IDs
            Cursor attrCursor = db.db().query(DatabaseHelper.ATTRIBUTES_TABLE,
                    new String[] {"_id"},
                    "title = ?",
                    new String[] { PURIFICATION_ATTRIBUTE },
                    null, null, null);
            
            ArrayList<String> attrIds = new ArrayList<>();
            while (attrCursor.moveToNext()) {
                attrIds.add(String.valueOf(attrCursor.getLong(0)));
            }
            attrCursor.close();

            if (!attrIds.isEmpty()) {
                cursor = db.db().query(DatabaseHelper.TRANSACTION_TABLE + " AS t" +
                        " JOIN " + DatabaseHelper.TRANSACTION_ATTRIBUTE_TABLE + " AS ta" +
                        " ON t._id = ta.transaction_id",
                        new String[] {
                            "t._id",
                            "t.datetime",
                            "t.from_amount",
                            "t.note",
                            "t.category_id",
                            "ta.value AS purification_value"
                        },
                        "ta.attribute_id IN (" + String.join(",", attrIds) + ") AND ta.value IS NOT NULL AND t.datetime > 0",
                        null,
                        null, null, "t.datetime");

            GraphUnit unit = new GraphUnit(0, PURIFICATION_ATTRIBUTE, currency, style);
            while (cursor.moveToNext()) {
                long amount = cursor.getLong(cursor.getColumnIndexOrThrow("from_amount"));
                String purificationValue = cursor.getString(cursor.getColumnIndexOrThrow("purification_value")); 
                
                if (purificationValue != null) {
                    try {
                        double pct = Double.parseDouble(purificationValue);
                        BigDecimal adjustedAmount = BigDecimal.valueOf(amount)
                            .multiply(BigDecimal.valueOf(pct))
                            .divide(BigDecimal.valueOf(100));
                        unit.addAmount(adjustedAmount, false);
                    } catch (NumberFormatException | ArithmeticException e) {
                        // Skip invalid values
                    }
                }
            }
            unit.flatten(IncomeExpense.BOTH);
            units.add(unit);
        }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return new ReportData(units, calculateTotal(units));
    }

    @Override
    public Criteria getCriteriaForId(DatabaseAdapter db, long id) {
        // Get all transactions that have a Purification attribute
        // Note: We explicitly select only parent transactions (parent_id=0) 
        // or splits with transfers (is_transfer=-1) to match the blotter view's logic
        String subQuery = "(SELECT t._id FROM transactions t" +
            " JOIN transaction_attribute ta ON ta.transaction_id=t._id" +
            " JOIN attributes a ON a._id=ta.attribute_id" +
            " WHERE a.title='" + PURIFICATION_ATTRIBUTE + "')";
            
        return Criteria.raw("(_id IN " + subQuery + " OR parent_id IN " + subQuery + ")");
    }

}
