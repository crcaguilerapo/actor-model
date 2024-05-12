package org.crcaguilerapo.adapters.out.memory;

import org.crcaguilerapo.dtos.PaymentDto;
import org.crcaguilerapo.port.out.IAccountPort;
import org.jooq.DSLContext;
import org.jooq.impl.SQLDataType;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

public class AccountRepository implements IAccountPort {

    private final DSLContext ctx;

    public AccountRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void pay(PaymentDto paymentDto) {

        ctx.transaction(configuration -> {
            var table = table("ACCOUNT");
            var numberAccount = field("NUMBER_ACCOUNT", SQLDataType.INTEGER);
            var balance = field("BALANCE", SQLDataType.INTEGER);

            var destination = ctx.select(numberAccount, balance)
                    .from(table)
                    .where(numberAccount.eq(paymentDto.destinationAccountNumber()))
                    .fetch()
                    .getFirst();

            var origin = ctx.select(numberAccount, balance)
                    .from(table)
                    .where(numberAccount.eq(paymentDto.originAccountNumber()))
                    .fetch()
                    .getFirst();

            ctx.update(table)
                    .set(balance, destination.getValue(balance) + paymentDto.amount())
                    .where(numberAccount.eq(destination.getValue(numberAccount)))
                    .execute();

            ctx.update(table)
                    .set(balance, origin.getValue(balance) - paymentDto.amount())
                    .where(numberAccount.eq(origin.getValue(numberAccount)))
                    .execute();
        });
    }

}
