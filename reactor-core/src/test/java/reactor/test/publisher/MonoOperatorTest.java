/*
 * Copyright (c) 2011-2017 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package reactor.test.publisher;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;


import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.annotation.Nullable;

/**
 * @author Stephane Maldini
 */
public abstract class MonoOperatorTest<I, O>
		extends BaseOperatorTest<I, Mono<I>, O, Mono<O>> {

	public final Scenario<I, O> scenario(Function<Mono<I>, ? extends Mono<O>> scenario) {
		if (defaultEmpty) {
			return Scenario.create(scenario)
			                                .applyAllOptions(defaultScenario.duplicate()
			                                                                .receiverEmpty());
		}
		return Scenario.create(scenario)
		                                .applyAllOptions(defaultScenario);
	}

	static public final class Scenario<I, O>
			extends OperatorScenario<I, Mono<I>, O, Mono<O>> {

		static <I, O> Scenario<I, O> create(Function<Mono<I>, ? extends Mono<O>> scenario) {
			return new Scenario<>(scenario, new Exception("scenario:"));
		}

		Scenario(@Nullable Function<Mono<I>, ? extends Mono<O>> scenario, @Nullable Exception stack) {
			super(scenario, stack);
		}

		@Override
		Scenario<I, O> duplicate() {
			return new Scenario<>(body, stack).applyAllOptions(this);
		}

		@Override
		public Scenario<I, O> shouldHitDropNextHookAfterTerminate(boolean shouldHitDropNextHookAfterTerminate) {
			super.shouldHitDropNextHookAfterTerminate(shouldHitDropNextHookAfterTerminate);
			return this;
		}

		@Override
		public Scenario<I, O> shouldHitDropErrorHookAfterTerminate(boolean shouldHitDropErrorHookAfterTerminate) {
			super.shouldHitDropErrorHookAfterTerminate(
					shouldHitDropErrorHookAfterTerminate);
			return this;
		}

		@Override
		public Scenario<I, O> shouldAssertPostTerminateState(boolean shouldAssertPostTerminateState) {
			super.shouldAssertPostTerminateState(shouldAssertPostTerminateState);
			return this;
		}

		@Override
		public Scenario<I, O> fusionMode(int fusionMode) {
			super.fusionMode(fusionMode);
			return this;
		}

		@Override
		public Scenario<I, O> fusionModeThreadBarrier(int fusionModeThreadBarrier) {
			super.fusionModeThreadBarrier(fusionModeThreadBarrier);
			return this;
		}

		@Override
		public Scenario<I, O> prefetch(int prefetch) {
			super.prefetch(prefetch);
			return this;
		}

		@Override
		public Scenario<I, O> producerEmpty() {
			super.producerEmpty();
			return this;
		}

		@Override
		public Scenario<I, O> producerNever() {
			super.producerNever();
			return this;
		}

		@Override
		public Scenario<I, O> producer(int n, IntFunction<? extends I> producer) {
			super.producer(n, producer);
			return this;
		}

		@Override
		public Scenario<I, O> producerError(RuntimeException e) {
			super.producerError(e);
			return this;
		}

		@Override
		public Scenario<I, O> droppedError(RuntimeException e) {
			super.droppedError(e);
			return this;
		}

		@Override
		public Scenario<I, O> droppedItem(I item) {
			super.droppedItem(item);
			return this;
		}

		@Override
		public final Scenario<I, O> receiverEmpty() {
			super.receiverEmpty();
			return this;
		}

		@Override
		@SafeVarargs
		public final Scenario<I, O> receive(Consumer<? super O>... receivers) {
			super.receive(receivers);
			return this;
		}

		@Override
		@SafeVarargs
		public final Scenario<I, O> receiveValues(O... receivers) {
			super.receiveValues(receivers);
			return this;
		}

		@Override
		public final Scenario<I, O> receive(int i, IntFunction<? extends O> receivers) {
			super.receive(i, receivers);
			return this;
		}

		@Override
		public Scenario<I, O> receiverDemand(long d) {
			super.receiverDemand(d);
			return this;
		}

		@Override
		public Scenario<I, O> description(String description) {
			super.description(description);
			return this;
		}

		@Override
		public Scenario<I, O> verifier(Consumer<StepVerifier.Step<O>> verifier) {
			super.verifier(verifier);
			return this;
		}

		@Override
		public Scenario<I, O> applyAllOptions(@Nullable OperatorScenario<I, Mono<I>, O, Mono<O>> source) {
			super.applyAllOptions(source);
			return this;
		}
	}

	@Override
	protected List<Scenario<I, O>> scenarios_operatorSuccess() {
		return Collections.emptyList();
	}

	@Override
	protected List<Scenario<I, O>> scenarios_operatorError() {
		return Collections.emptyList();
	}

	@Override
	protected List<Scenario<I, O>> scenarios_errorFromUpstreamFailure() {
		return scenarios_operatorSuccess();
	}

	@Override
	protected List<Scenario<I, O>> scenarios_touchAndAssertState() {
		return scenarios_operatorSuccess();
	}

	@Override
	@SuppressWarnings("unchecked")
	protected final OperatorScenario<I, Mono<I>, O, Mono<O>> defaultScenarioOptions(
			OperatorScenario<I, Mono<I>, O, Mono<O>> defaultOptions) {
		MonoOperatorTest.Scenario<I, O>
				s = new MonoOperatorTest.Scenario<I, O>(null, null).applyAllOptions(defaultOptions)
				                                                   .producer(1, i -> (I) "test")
				                                                   .receive(1, i -> (O)"test" )
				                                                   .droppedError(new RuntimeException("dropped"))
				                                                   .droppedItem((I)"dropped");
		this.defaultScenario = s;
		return defaultScenarioOptions(s);
	}

	@SuppressWarnings("unchecked")
	protected MonoOperatorTest.Scenario<I, O> defaultScenarioOptions(MonoOperatorTest.Scenario<I, O> defaultOptions) {
		return defaultOptions;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Mono<I> sourceScalar(OperatorScenario<I, Mono<I>, O, Mono<O>> scenario) {
		if(scenario.producerCount() == 0){
			return (Mono<I>)Mono.empty();
		}
		return Mono.just(scenario.producingMapper.apply(0));
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Mono<I> sourceCallable(OperatorScenario<I, Mono<I>, O, Mono<O>> scenario) {
		if(scenario.producerCount() == 0){
			return (Mono<I>)Mono.fromRunnable(() -> {});
		}
		return Mono.fromCallable(() -> scenario.producingMapper.apply(0));
	}

	@Override
	protected Mono<I> withFluxSource(Flux<I> input) {
		return Mono.fromDirect(input);
	}

	@Override
	protected Mono<I> hide(Mono<I> input) {
		return input.hide();
	}

	@Override
	protected Mono<O> conditional(Mono<O> output) {
		return output.filter(t -> true);
	}

	@Override
	protected Mono<O> doOnSubscribe(Mono<O> output,
			Consumer<? super Subscription> doOnSubscribe) {
		return output.doOnSubscribe(doOnSubscribe);
	}
}
