package org.particleframework.tck

import junit.framework.TestCase
import org.particleframework.tck.accessories.Cupholder
import org.particleframework.tck.accessories.RoundThing
import org.particleframework.tck.accessories.SpareTire

import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

class Convertible implements Car {

    @Inject @Drivers Seat driversSeatA
    @Inject @Drivers Seat driversSeatB
    @Inject SpareTire spareTire
    @Inject Cupholder cupholder
    @Inject Provider<Engine> engineProvider

    private boolean methodWithZeroParamsInjected
    private boolean methodWithMultipleParamsInjected
    private boolean methodWithNonVoidReturnInjected

    private Seat constructorPlainSeat
    private Seat constructorDriversSeat
    private Tire constructorPlainTire
    private Tire constructorSpareTire
    private Provider<Seat> constructorPlainSeatProvider = nullProvider()
    private Provider<Seat> constructorDriversSeatProvider = nullProvider()
    private Provider<Tire> constructorPlainTireProvider = nullProvider()
    private Provider<Tire> constructorSpareTireProvider = nullProvider()

    @Inject Seat fieldPlainSeat
    @Inject @Drivers Seat fieldDriversSeat
    @Inject Tire fieldPlainTire
    @Inject @Named("spare") Tire fieldSpareTire
    @Inject Provider<Seat> fieldPlainSeatProvider = nullProvider()
    @Inject @Drivers Provider<Seat> fieldDriversSeatProvider = nullProvider()
    @Inject Provider<Tire> fieldPlainTireProvider = nullProvider()
    @Inject @Named("spare") Provider<Tire> fieldSpareTireProvider = nullProvider()

    private Seat methodPlainSeat
    private Seat methodDriversSeat
    private Tire methodPlainTire
    private Tire methodSpareTire
    private Provider<Seat> methodPlainSeatProvider = nullProvider()
    private Provider<Seat> methodDriversSeatProvider = nullProvider()
    private Provider<Tire> methodPlainTireProvider = nullProvider()
    private Provider<Tire> methodSpareTireProvider = nullProvider()

    @Inject static Seat staticFieldPlainSeat
    @Inject @Drivers static Seat staticFieldDriversSeat
    @Inject static Tire staticFieldPlainTire
    @Inject @Named("spare") static Tire staticFieldSpareTire
    @Inject static Provider<Seat> staticFieldPlainSeatProvider = nullProvider()
    @Inject @Drivers static Provider<Seat> staticFieldDriversSeatProvider = nullProvider()
    @Inject static Provider<Tire> staticFieldPlainTireProvider = nullProvider()
    @Inject @Named("spare") static Provider<Tire> staticFieldSpareTireProvider = nullProvider()

    private static Seat staticMethodPlainSeat
    private static Seat staticMethodDriversSeat
    private static Tire staticMethodPlainTire
    private static Tire staticMethodSpareTire
    private static Provider<Seat> staticMethodPlainSeatProvider = nullProvider()
    private static Provider<Seat> staticMethodDriversSeatProvider = nullProvider()
    private static Provider<Tire> staticMethodPlainTireProvider = nullProvider()
    private static Provider<Tire> staticMethodSpareTireProvider = nullProvider()

    @Inject Convertible(
            Seat plainSeat,
            @Drivers Seat driversSeat,
            Tire plainTire,
            @Named("spare") Tire spareTire,
            Provider<Seat> plainSeatProvider,
            @Drivers Provider<Seat> driversSeatProvider,
            Provider<Tire> plainTireProvider,
            @Named("spare") Provider<Tire> spareTireProvider) {
        constructorPlainSeat = plainSeat
        constructorDriversSeat = driversSeat
        constructorPlainTire = plainTire
        constructorSpareTire = spareTire
        constructorPlainSeatProvider = plainSeatProvider
        constructorDriversSeatProvider = driversSeatProvider
        constructorPlainTireProvider = plainTireProvider
        constructorSpareTireProvider = spareTireProvider
    }

    Convertible() {
        throw new AssertionError("Unexpected call to non-injectable constructor")
    }

    void setSeat(Seat unused) {
        throw new AssertionError("Unexpected call to non-injectable method")
    }

    @Inject void injectMethodWithZeroArgs() {
        methodWithZeroParamsInjected = true
    }

    @Inject String injectMethodWithNonVoidReturn() {
        methodWithNonVoidReturnInjected = true
        return "unused"
    }

    @Inject void injectInstanceMethodWithManyArgs(
            Seat plainSeat,
            @Drivers Seat driversSeat,
            Tire plainTire,
            @Named("spare") Tire spareTire,
            Provider<Seat> plainSeatProvider,
            @Drivers Provider<Seat> driversSeatProvider,
            Provider<Tire> plainTireProvider,
            @Named("spare") Provider<Tire> spareTireProvider) {
        methodWithMultipleParamsInjected = true

        methodPlainSeat = plainSeat
        methodDriversSeat = driversSeat
        methodPlainTire = plainTire
        methodSpareTire = spareTire
        methodPlainSeatProvider = plainSeatProvider
        methodDriversSeatProvider = driversSeatProvider
        methodPlainTireProvider = plainTireProvider
        methodSpareTireProvider = spareTireProvider
    }

    @Inject static void injectStaticMethodWithManyArgs(
            Seat plainSeat,
            @Drivers Seat driversSeat,
            Tire plainTire,
            @Named("spare") Tire spareTire,
            Provider<Seat> plainSeatProvider,
            @Drivers Provider<Seat> driversSeatProvider,
            Provider<Tire> plainTireProvider,
            @Named("spare") Provider<Tire> spareTireProvider) {
        staticMethodPlainSeat = plainSeat
        staticMethodDriversSeat = driversSeat
        staticMethodPlainTire = plainTire
        staticMethodSpareTire = spareTire
        staticMethodPlainSeatProvider = plainSeatProvider
        staticMethodDriversSeatProvider = driversSeatProvider
        staticMethodPlainTireProvider = plainTireProvider
        staticMethodSpareTireProvider = spareTireProvider
    }

    /**
     * Returns a provider that always returns null. This is used as a default
     * value to avoid null checks for omitted provider injections.
     */
    private static <T> Provider<T> nullProvider() {
        return new NullProvider<T>()
    }

    static class NullProvider<T> implements Provider<T> {

        @Override
        T get() {
            return null
        }
    }

    public static ThreadLocal<Convertible> localConvertible = new ThreadLocal<Convertible>()

    static class Tests extends TestCase {

        private final Convertible car = localConvertible.get()
        private final Cupholder cupholder = car.cupholder
        private final SpareTire spareTire = car.spareTire
        private final Tire plainTire = car.fieldPlainTire
        private final Engine engine = car.engineProvider.get()

        // smoke tests: if these fail all bets are off

        void testFieldsInjected() {
            assertTrue(cupholder != null && spareTire != null)
        }

        void testProviderReturnedValues() {
            assertTrue(engine != null)
        }

        // injecting different kinds of members

        void testMethodWithZeroParametersInjected() {
            assertTrue(car.methodWithZeroParamsInjected)
        }

        void testMethodWithMultipleParametersInjected() {
            assertTrue(car.methodWithMultipleParamsInjected)
        }

        void testNonVoidMethodInjected() {
            assertTrue(car.methodWithNonVoidReturnInjected)
        }

        void testPublicNoArgsConstructorInjected() {
            assertTrue(engine.publicNoArgsConstructorInjected)
        }

        void testSubtypeFieldsInjected() {
            assertTrue(spareTire.hasSpareTireBeenFieldInjected())
        }

        void testSubtypeMethodsInjected() {
            assertTrue(spareTire.hasSpareTireBeenMethodInjected())
        }

        void testSupertypeFieldsInjected() {
            assertTrue(spareTire.hasTireBeenFieldInjected())
        }

        void testSupertypeMethodsInjected() {
            assertTrue(spareTire.hasTireBeenMethodInjected())
        }

        void testTwiceOverriddenMethodInjectedWhenMiddleLacksAnnotation() {
            assertTrue(engine.overriddenTwiceWithOmissionInMiddleInjected)
        }

        // injected values

        void testQualifiersNotInheritedFromOverriddenMethod() {
            assertFalse(engine.qualifiersInheritedFromOverriddenMethod)
        }

        void testConstructorInjectionWithValues() {
            assertFalse("Expected unqualified value",
                    car.constructorPlainSeat instanceof DriversSeat)
            assertFalse("Expected unqualified value",
                    car.constructorPlainTire instanceof SpareTire)
            assertTrue("Expected qualified value",
                    car.constructorDriversSeat instanceof DriversSeat)
            assertTrue("Expected qualified value",
                    car.constructorSpareTire instanceof SpareTire)
        }

        void testFieldInjectionWithValues() {
            assertFalse("Expected unqualified value",
                    car.fieldPlainSeat instanceof DriversSeat)
            assertFalse("Expected unqualified value",
                    car.fieldPlainTire instanceof SpareTire)
            assertTrue("Expected qualified value",
                    car.fieldDriversSeat instanceof DriversSeat)
            assertTrue("Expected qualified value",
                    car.fieldSpareTire instanceof SpareTire)
        }

        void testMethodInjectionWithValues() {
            assertFalse("Expected unqualified value",
                    car.methodPlainSeat instanceof DriversSeat)
            assertFalse("Expected unqualified value",
                    car.methodPlainTire instanceof SpareTire)
            assertTrue("Expected qualified value",
                    car.methodDriversSeat instanceof DriversSeat)
            assertTrue("Expected qualified value",
                    car.methodSpareTire instanceof SpareTire)
        }

        // injected providers

        void testConstructorInjectionWithProviders() {
            assertFalse("Expected unqualified value",
                    car.constructorPlainSeatProvider.get() instanceof DriversSeat)
            assertFalse("Expected unqualified value",
                    car.constructorPlainTireProvider.get() instanceof SpareTire)
            assertTrue("Expected qualified value",
                    car.constructorDriversSeatProvider.get() instanceof DriversSeat)
            assertTrue("Expected qualified value",
                    car.constructorSpareTireProvider.get() instanceof SpareTire)
        }

        void testFieldInjectionWithProviders() {
            assertFalse("Expected unqualified value",
                    car.fieldPlainSeatProvider.get() instanceof DriversSeat)
            assertFalse("Expected unqualified value",
                    car.fieldPlainTireProvider.get() instanceof SpareTire)
            assertTrue("Expected qualified value",
                    car.fieldDriversSeatProvider.get() instanceof DriversSeat)
            assertTrue("Expected qualified value",
                    car.fieldSpareTireProvider.get() instanceof SpareTire)
        }

        void testMethodInjectionWithProviders() {
            assertFalse("Expected unqualified value",
                    car.methodPlainSeatProvider.get() instanceof DriversSeat)
            assertFalse("Expected unqualified value",
                    car.methodPlainTireProvider.get() instanceof SpareTire)
            assertTrue("Expected qualified value",
                    car.methodDriversSeatProvider.get() instanceof DriversSeat)
            assertTrue("Expected qualified value",
                    car.methodSpareTireProvider.get() instanceof SpareTire)
        }


        // singletons

        void testConstructorInjectedProviderYieldsSingleton() {
            assertSame("Expected same value",
                    car.constructorPlainSeatProvider.get(), car.constructorPlainSeatProvider.get())
        }

        void testFieldInjectedProviderYieldsSingleton() {
            assertSame("Expected same value",
                    car.fieldPlainSeatProvider.get(), car.fieldPlainSeatProvider.get())
        }

        void testMethodInjectedProviderYieldsSingleton() {
            assertSame("Expected same value",
                    car.methodPlainSeatProvider.get(), car.methodPlainSeatProvider.get())
        }

        void testCircularlyDependentSingletons() {
            // uses provider.get() to get around circular deps
            assertSame(cupholder.seatProvider.get().getCupholder(), cupholder)
        }


        // non singletons

        void testSingletonAnnotationNotInheritedFromSupertype() {
            assertNotSame(car.driversSeatA, car.driversSeatB)
        }

        void testConstructorInjectedProviderYieldsDistinctValues() {
            assertNotSame("Expected distinct values",
                    car.constructorDriversSeatProvider.get(), car.constructorDriversSeatProvider.get())
            assertNotSame("Expected distinct values",
                    car.constructorPlainTireProvider.get(), car.constructorPlainTireProvider.get())
            assertNotSame("Expected distinct values",
                    car.constructorSpareTireProvider.get(), car.constructorSpareTireProvider.get())
        }

        void testFieldInjectedProviderYieldsDistinctValues() {
            assertNotSame("Expected distinct values",
                    car.fieldDriversSeatProvider.get(), car.fieldDriversSeatProvider.get())
            assertNotSame("Expected distinct values",
                    car.fieldPlainTireProvider.get(), car.fieldPlainTireProvider.get())
            assertNotSame("Expected distinct values",
                    car.fieldSpareTireProvider.get(), car.fieldSpareTireProvider.get())
        }

        void testMethodInjectedProviderYieldsDistinctValues() {
            assertNotSame("Expected distinct values",
                    car.methodDriversSeatProvider.get(), car.methodDriversSeatProvider.get())
            assertNotSame("Expected distinct values",
                    car.methodPlainTireProvider.get(), car.methodPlainTireProvider.get())
            assertNotSame("Expected distinct values",
                    car.methodSpareTireProvider.get(), car.methodSpareTireProvider.get())
        }


        // mix inheritance + visibility

        void testPackagePrivateMethodInjectedDifferentPackages() {
            assertTrue(spareTire.subPackagePrivateMethodInjected)
            assertTrue(spareTire.superPackagePrivateMethodInjected)
        }

        void testOverriddenProtectedMethodInjection() {
            assertTrue(spareTire.subProtectedMethodInjected)
            assertFalse(spareTire.superProtectedMethodInjected)
        }

        void testOverriddenPublicMethodNotInjected() {
            assertTrue(spareTire.subPublicMethodInjected)
            assertFalse(spareTire.superPublicMethodInjected)
        }


        // inject in order

        void testFieldsInjectedBeforeMethods() {
            assertFalse(spareTire.methodInjectedBeforeFields)
        }

        void testSupertypeMethodsInjectedBeforeSubtypeFields() {
            assertFalse(spareTire.subtypeFieldInjectedBeforeSupertypeMethods)
        }

        void testSupertypeMethodInjectedBeforeSubtypeMethods() {
            assertFalse(spareTire.subtypeMethodInjectedBeforeSupertypeMethods)
        }


        // necessary injections occur

        void testPackagePrivateMethodInjectedEvenWhenSimilarMethodLacksAnnotation() {
            assertTrue(spareTire.subPackagePrivateMethodForOverrideInjected)
        }


        // override or similar method without @Inject

        void testPrivateMethodNotInjectedWhenSupertypeHasAnnotatedSimilarMethod() {
            assertFalse(spareTire.superPrivateMethodForOverrideInjected)
        }

        void testPackagePrivateMethodNotInjectedWhenOverrideLacksAnnotation() {
            assertFalse(engine.subPackagePrivateMethodForOverrideInjected)
            assertFalse(engine.superPackagePrivateMethodForOverrideInjected)
        }

        void testPackagePrivateMethodNotInjectedWhenSupertypeHasAnnotatedSimilarMethod() {
            assertFalse(spareTire.superPackagePrivateMethodForOverrideInjected)
        }

        void testProtectedMethodNotInjectedWhenOverrideNotAnnotated() {
            assertFalse(spareTire.protectedMethodForOverrideInjected)
        }

        void testPublicMethodNotInjectedWhenOverrideNotAnnotated() {
            assertFalse(spareTire.publicMethodForOverrideInjected)
        }

        void testTwiceOverriddenMethodNotInjectedWhenOverrideLacksAnnotation() {
            assertFalse(engine.overriddenTwiceWithOmissionInSubclassInjected)
        }

        void testOverriddingMixedWithPackagePrivate2() {
            assertTrue(spareTire.packagePrivateMethod2Injected)
            assertTrue(((Tire) spareTire).packagePrivateMethod2Injected)
            assertFalse(((RoundThing) spareTire).packagePrivateMethod2Injected)

            assertTrue(plainTire.packagePrivateMethod2Injected)
            assertTrue(((RoundThing) plainTire).packagePrivateMethod2Injected)
        }

        void testOverriddingMixedWithPackagePrivate3() {
            assertFalse(spareTire.packagePrivateMethod3Injected)
            assertTrue(((Tire) spareTire).packagePrivateMethod3Injected)
            assertFalse(((RoundThing) spareTire).packagePrivateMethod3Injected)

            assertTrue(plainTire.packagePrivateMethod3Injected)
            assertTrue(((RoundThing) plainTire).packagePrivateMethod3Injected)
        }

        void testOverriddingMixedWithPackagePrivate4() {
            assertFalse(plainTire.packagePrivateMethod4Injected)
            assertTrue(((RoundThing) plainTire).packagePrivateMethod4Injected)
        }

        // inject only once

        void testOverriddenPackagePrivateMethodInjectedOnlyOnce() {
            assertFalse(engine.overriddenPackagePrivateMethodInjectedTwice)
        }

        void testSimilarPackagePrivateMethodInjectedOnlyOnce() {
            assertFalse(spareTire.similarPackagePrivateMethodInjectedTwice)
        }

        void testOverriddenProtectedMethodInjectedOnlyOnce() {
            assertFalse(spareTire.overriddenProtectedMethodInjectedTwice)
        }

        void testOverriddenPublicMethodInjectedOnlyOnce() {
            assertFalse(spareTire.overriddenPublicMethodInjectedTwice)
        }

    }

    static class StaticTests extends TestCase {

        void testSubtypeStaticFieldsInjected() {
            assertTrue(SpareTire.hasBeenStaticFieldInjected())
        }

        void testSubtypeStaticMethodsInjected() {
            assertTrue(SpareTire.hasBeenStaticMethodInjected())
        }

        void testSupertypeStaticFieldsInjected() {
            assertTrue(Tire.hasBeenStaticFieldInjected())
        }

        void testSupertypeStaticMethodsInjected() {
            assertTrue(Tire.hasBeenStaticMethodInjected())
        }

        void testStaticFieldInjectionWithValues() {
            assertFalse("Expected unqualified value",
                    staticFieldPlainSeat instanceof DriversSeat)
            assertFalse("Expected unqualified value",
                    staticFieldPlainTire instanceof SpareTire)
            assertTrue("Expected qualified value",
                    staticFieldDriversSeat instanceof DriversSeat)
            assertTrue("Expected qualified value",
                    staticFieldSpareTire instanceof SpareTire)
        }

        void testStaticMethodInjectionWithValues() {
            assertFalse("Expected unqualified value",
                    staticMethodPlainSeat instanceof DriversSeat)
            assertFalse("Expected unqualified value",
                    staticMethodPlainTire instanceof SpareTire)
            assertTrue("Expected qualified value",
                    staticMethodDriversSeat instanceof DriversSeat)
            assertTrue("Expected qualified value",
                    staticMethodSpareTire instanceof SpareTire)
        }

        void testStaticFieldsInjectedBeforeMethods() {
            assertFalse(SpareTire.staticMethodInjectedBeforeStaticFields)
        }

        void testSupertypeStaticMethodsInjectedBeforeSubtypeStaticFields() {
            assertFalse(SpareTire.subtypeStaticFieldInjectedBeforeSupertypeStaticMethods)
        }

        void testSupertypeStaticMethodsInjectedBeforeSubtypeStaticMethods() {
            assertFalse(SpareTire.subtypeStaticMethodInjectedBeforeSupertypeStaticMethods)
        }

        void testStaticFieldInjectionWithProviders() {
            assertFalse("Expected unqualified value",
                    staticFieldPlainSeatProvider.get() instanceof DriversSeat)
            assertFalse("Expected unqualified value",
                    staticFieldPlainTireProvider.get() instanceof SpareTire)
            assertTrue("Expected qualified value",
                    staticFieldDriversSeatProvider.get() instanceof DriversSeat)
            assertTrue("Expected qualified value",
                    staticFieldSpareTireProvider.get() instanceof SpareTire)
        }

        void testStaticMethodInjectionWithProviders() {
            assertFalse("Expected unqualified value",
                    staticMethodPlainSeatProvider.get() instanceof DriversSeat)
            assertFalse("Expected unqualified value",
                    staticMethodPlainTireProvider.get() instanceof SpareTire)
            assertTrue("Expected qualified value",
                    staticMethodDriversSeatProvider.get() instanceof DriversSeat)
            assertTrue("Expected qualified value",
                    staticMethodSpareTireProvider.get() instanceof SpareTire)
        }
    }

    static class PrivateTests extends TestCase {

        private final Convertible car = localConvertible.get()
        private final Engine engine = car.engineProvider.get()
        private final SpareTire spareTire = car.spareTire

        void testSupertypePrivateMethodInjected() {
            assertTrue(spareTire.superPrivateMethodInjected)
            assertTrue(spareTire.subPrivateMethodInjected)
        }

        void testPackagePrivateMethodInjectedSamePackage() {
            assertTrue(engine.subPackagePrivateMethodInjected)
            assertFalse(engine.superPackagePrivateMethodInjected)
        }

        void testPrivateMethodInjectedEvenWhenSimilarMethodLacksAnnotation() {
            assertTrue(spareTire.subPrivateMethodForOverrideInjected)
        }

        void testSimilarPrivateMethodInjectedOnlyOnce() {
            assertFalse(spareTire.similarPrivateMethodInjectedTwice)
        }
    }
}